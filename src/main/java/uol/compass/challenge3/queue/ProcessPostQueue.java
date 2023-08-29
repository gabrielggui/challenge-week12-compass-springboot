package uol.compass.challenge3.queue;

import feign.RetryableException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uol.compass.challenge3.api.PostApiClient;
import uol.compass.challenge3.entity.Comment;
import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.service.PostService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessPostQueue {

    private PostQueue postQueue;
    private PostService postService;
    private PostApiClient postApiClient;

    public ProcessPostQueue(PostQueue postQueue, PostService postService, PostApiClient postApiClient) {
        this.postQueue = postQueue;
        this.postService = postService;
        this.postApiClient = postApiClient;
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void processCreatedQueue() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.CREATED).drainTo(posts);

        posts.parallelStream().forEach(post -> {
            post.getStates().add(new State(Status.POST_FIND, post));

            Post updatedPost = postService.update(post);
            postQueue.insertIntoQueue(QueueType.POST_FIND, updatedPost);
            processPostFindQueue();
        });
    }

    @Transactional
    public void processPostFindQueue() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.POST_FIND).drainTo(posts);

        posts.parallelStream().forEach(post -> {
            try {
                Post postApi = postApiClient.findPostById(post.getId());
                post.setTitle(postApi.getTitle());
                post.setBody(postApi.getBody());
                post.getStates().add(new State(Status.POST_OK, post));

                Post updatedPost = postService.update(post);
                postQueue.insertIntoQueue(QueueType.POST_OK, updatedPost);
            } catch (RetryableException e) {
                System.err.println(e.getMessage());
                markPostAsFailedAndDisable(post);
            }
        });
    }

    @Transactional
    @Scheduled(fixedDelay = 7500)
    public void processPostOkQueue() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.POST_OK).drainTo(posts);

        posts.parallelStream().forEach(post -> {
            post.getStates().add(new State(Status.COMMENTS_FIND, post));

            Post updatedPost = postService.update(post);
            postQueue.insertIntoQueue(QueueType.COMMENTS_FIND, updatedPost);
            processCommentsFindQueue();
        });
    }

    @Transactional
    public void processCommentsFindQueue() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.COMMENTS_FIND).drainTo(posts);

        posts.parallelStream().forEach(post -> {
            try {
                List<Comment> commentApi = postApiClient.findCommentsByPostId(post.getId());
                commentApi.forEach(c -> c.setPost(post));
                post.setComments(commentApi);
                post.getStates().add(new State(Status.COMMENTS_OK, post));

                Post updatedPost = postService.update(post);
                postQueue.insertIntoQueue(QueueType.COMMENTS_OK, updatedPost);
                markPostAsEnabled(post);
            } catch (RetryableException e) {
                System.err.println(e.getMessage());
                markPostAsFailedAndDisable(post);
            }
        });
    }

    @Transactional
    public Post markPostAsEnabled(Post post) {
        post.getStates().add(new State(Status.ENABLED, post));
        return postService.update(post);
    }

    @Transactional
    public Post markPostAsFailedAndDisable(Post post) {
        post.getStates().add(new State(Status.FAILED, post));
        markPostAsDisabled(post);
        return postService.update(post);
    }

    @Transactional
    public Post markPostAsDisabled(Post post) {
        post.getStates().add(new State(Status.DISABLED, post));
        return postService.update(post);
    }
}
