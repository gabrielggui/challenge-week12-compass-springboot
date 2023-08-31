package uol.compass.challenge3.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.service.ApiPostService;
import uol.compass.challenge3.service.PostService;

@Component
public class PostConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PostConsumer.class);

    private ApiPostService apiPostService;
    private PostService postService;
    private ObjectMapper objectMapper;

    public PostConsumer(ApiPostService apiPostService, PostService postService, ObjectMapper objectMapper) {
        this.apiPostService = apiPostService;
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = { "CREATED", "UPDATING" }, concurrency = "5")
    public void processQueue(String message) throws JsonProcessingException {
        logger.info("Received message from queue: {}", message);
        
        Post post = this.jsonToPost(message);
        post = postService.updatePostState(post, Status.POST_FIND);

        try {
            post = apiPostService.updatePostFromExternalAPI(post);
            post = postService.updatePostState(post, Status.POST_OK);
            post = postService.updatePostState(post, Status.COMMENTS_FIND);
            post = apiPostService.updatePostCommentsFromExternalAPI(post);
            post = postService.updatePostState(post, Status.COMMENTS_OK);
            post = postService.updatePostState(post, Status.ENABLED);
            postService.update(post);
        } catch (Exception e) {
            logger.error("Error processing post: {}", e.getMessage());
            post = postService.updatePostState(post, Status.FAILED);
            post = postService.updatePostState(post, Status.DISABLED);
            postService.update(post);
        }
    }

    private Post jsonToPost(String json) throws JsonProcessingException {
        logger.info("Converting JSON to post");

        return objectMapper.readValue(json, Post.class);
    }

}
