package uol.compass.challenge3.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;

@Component
public class PostUtils {

    private final Logger logger = LoggerFactory.getLogger(PostUtils.class);

    private final ObjectMapper objectMapper;

    public PostUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Post updatePostState(Post post, Status status) {
        logger.info("Updating post state to: {} for Post ID: {}", status, post.getId());
        post.getStates().add(new State(status, post));
        return post;
    }

    public Post jsonToPost(String json) throws JsonProcessingException {
        logger.info("Converting JSON to post");

        return objectMapper.readValue(json, Post.class);
    }

    public String postToJson(Post post) throws JsonProcessingException {
        logger.info("Converting post to JSON");

        return objectMapper.writeValueAsString(post);
    }
}
