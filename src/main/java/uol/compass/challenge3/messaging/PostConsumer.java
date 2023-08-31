package uol.compass.challenge3.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import uol.compass.challenge3.api.PostApiClient;
import uol.compass.challenge3.entity.Comment;
import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.service.PostService;

import java.util.List;

@Component
public class PostConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PostConsumer.class);

    private PostApiClient postApiClient;
    private PostService postService;
    private ObjectMapper objectMapper;
    private PostProducer postProducer;

    public PostConsumer(PostApiClient postApiClient, PostService postService, ObjectMapper objectMapper,
            PostProducer postProducer) {
        this.postApiClient = postApiClient;
        this.postService = postService;
        this.objectMapper = objectMapper;
        this.postProducer = postProducer;
    }

    @RabbitListener(queues = { "CREATED", "UPDATING" }, concurrency = "2")
    public void processCreatedAndUpdatingQueue(String message) throws JsonProcessingException {
        logger.info("Received message for queues CREATED and UPDATING: {}", message);

        postProducer.sendToQueue(message, QueueType.POST_FIND);
    }

    @RabbitListener(queues = "POST_FIND", concurrency = "5")
    public void processPostFindQueue(String message) throws JsonProcessingException {
        Post post = jsonToPost(message);
        post.getStates().add(new State(Status.POST_FIND, post));

        logger.info("Received message for queue POST_FIND: {}", post);

        try {
            Post postApi = postApiClient.findPostById(post.getId());
            post.setTitle(postApi.getTitle());
            post.setBody(postApi.getBody());
            post.getStates().add(new State(Status.POST_OK, post));

            logger.info("Processed message successfully for queue POST_FIND: {}", post);

            postProducer.sendToQueue(post, QueueType.POST_OK);
        } catch (Exception e) {
            logger.error("Error processing message for queue POST_FIND: {}", e.getMessage());
            postProducer.sendToQueue(post, QueueType.FAILED);
        }
    }

    @RabbitListener(queues = "POST_OK", concurrency = "2")
    public void processPostOkQueue(String message) throws JsonProcessingException {
        logger.info("Received message for queue POST_OK: {}", message);

        postProducer.sendToQueue(message, QueueType.COMMENTS_FIND);
    }

    @RabbitListener(queues = "COMMENTS_FIND", concurrency = "5")
    public void processCommentsFindQueue(String message) throws JsonProcessingException {
        Post post = jsonToPost(message);
        post.getStates().add(new State(Status.COMMENTS_FIND, post));

        logger.info("Received message for queue COMMENTS_FIND: {}", post);

        try {
            List<Comment> commentApi = postApiClient.findCommentsByPostId(post.getId());
            commentApi.forEach(c -> c.setPost(post));
            post.setComments(commentApi);
            post.getStates().add(new State(Status.COMMENTS_OK, post));

            logger.info("Processed message successfully for queue COMMENTS_FIND: {}", post);

            postProducer.sendToQueue(post, QueueType.COMMENTS_OK);
        } catch (Exception e) {
            logger.error("Error processing message for queue COMMENTS_FIND: {}", e.getMessage());
            postProducer.sendToQueue(post, QueueType.FAILED);
        }
    }

    @RabbitListener(queues = "COMMENTS_OK", concurrency = "2")
    public void processCommentsOkQueue(String message) throws JsonProcessingException {
        Post post = jsonToPost(message);

        logger.info("Received message for queue COMMENTS_OK: {}", post);

        post.getStates().add(new State(Status.ENABLED, post));
        postProducer.sendToQueue(post, QueueType.ENABLED);
    }

    @RabbitListener(queues = { "ENABLED", "DISABLED" })
    public void persistEnabledAndDisabledPosts(String message) throws JsonProcessingException {
        Post post = jsonToPost(message);

        logger.info("Received message for queues ENABLED and DISABLED: {}", post);

        postService.update(post);
    }

    @RabbitListener(queues = "FAILED", concurrency = "2")
    public void processFailedQueueAndDisablePost(String message) throws JsonProcessingException {
        Post post = jsonToPost(message);
        post.getStates().add(new State(Status.FAILED, post));
        post.getStates().add(new State(Status.DISABLED, post));

        logger.info("Received message for queue FAILED: {}", post);

        postProducer.sendToQueue(post, QueueType.DISABLED);
    }

    private Post jsonToPost(String json) throws JsonProcessingException {
        logger.info("Converting JSON to post");

        return objectMapper.readValue(json, Post.class);
    }

}
