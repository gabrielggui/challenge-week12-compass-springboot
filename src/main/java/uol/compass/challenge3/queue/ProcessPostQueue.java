package uol.compass.challenge3.queue;

import feign.RetryableException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uol.compass.challenge3.api.PostApiClient;
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
    @Scheduled(fixedDelay = 10000)
    public void processCreatedPosts() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.CREATED).drainTo(posts);

        posts.parallelStream().forEach(post -> {
            Post postFromDatabase = postService.findById(post.getId());
            postFromDatabase.getStates().add(new State(Status.POST_FIND, postFromDatabase));


            Post updatedPost = postService.update(postFromDatabase);
            postQueue.insertIntoQueue(QueueType.POST_FIND, updatedPost);
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void processPostFindPosts() {
        List<Post> posts = new ArrayList<>();
        postQueue.findQueueByType(QueueType.POST_FIND).drainTo(posts);
        posts.parallelStream().forEach(post -> {
            try {
                Post postApi = postApiClient.findPostById(post.getId());
                post.setTitle(postApi.getTitle());
                post.setBody(postApi.getBody());
                post.getStates().add(new State(Status.POST_OK, post));

                postService.update(post);
            } catch (RetryableException e) {
                System.err.println(e.getMessage());
                post.getStates().add(new State(Status.FAILED, post));
                postService.update(post);
            }
        });

    }
}

