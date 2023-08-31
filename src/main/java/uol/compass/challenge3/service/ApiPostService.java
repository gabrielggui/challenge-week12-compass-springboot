package uol.compass.challenge3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uol.compass.challenge3.api.PostApiClient;
import uol.compass.challenge3.entity.Comment;
import uol.compass.challenge3.entity.Post;

@Service
public class ApiPostService {

    private final Logger logger = LoggerFactory.getLogger(ApiPostService.class);

    private final PostApiClient postApiClient;

    public ApiPostService(PostApiClient postApiClient) {
        this.postApiClient = postApiClient;
    }

    public Post updatePostFromExternalAPI(Post post) {
        logger.info("Updating post from external API. Post ID: {}", post.getId());

        Post postApi = postApiClient.findPostById(post.getId());
        post.setTitle(postApi.getTitle());
        post.setBody(postApi.getBody());

        logger.info("Post updated successfully.");
        return post;
    }

    public Post updatePostCommentsFromExternalAPI(Post post) {
        logger.info("Updating post comments from external API. Post ID: {}", post.getId());

        List<Comment> commentApi = postApiClient.findCommentsByPostId(post.getId());
        commentApi.forEach(c -> c.setPost(post));
        post.setComments(commentApi);

        logger.info("Post comments updated successfully.");
        return post;
    }
}
