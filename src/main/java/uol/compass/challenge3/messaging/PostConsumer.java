package uol.compass.challenge3.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.service.ApiPostService;
import uol.compass.challenge3.service.PostService;
import uol.compass.challenge3.utils.PostUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PostConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PostConsumer.class);

    private final ApiPostService apiPostService;
    private final PostService postService;
    private final PostUtils postUtils;

    public PostConsumer(ApiPostService apiPostService, PostService postService, PostUtils postUtils) {
        this.apiPostService = apiPostService;
        this.postService = postService;
        this.postUtils = postUtils;
    }

    @JmsListener(destination = "CREATED")
    @JmsListener(destination = "UPDATING")
    public void processQueue(String message) throws JsonProcessingException {
        logger.info("Received message from queue: {}", message);

        Post post = postUtils.jsonToPost(message);
        post = postUtils.updatePostState(post, Status.POST_FIND);

        try {
            post = apiPostService.updatePostFromExternalAPI(post);
            post = postUtils.updatePostState(post, Status.POST_OK);
            post = postUtils.updatePostState(post, Status.COMMENTS_FIND);
            post = apiPostService.updatePostCommentsFromExternalAPI(post);
            post = postUtils.updatePostState(post, Status.COMMENTS_OK);
            post = postUtils.updatePostState(post, Status.ENABLED);
            postService.update(post);
        } catch (Exception e) {
            logger.error("Error processing post: {}", e.getMessage());
            post = postUtils.updatePostState(post, Status.FAILED);
            post = postUtils.updatePostState(post, Status.DISABLED);
            postService.update(post);
        }
    }
}
