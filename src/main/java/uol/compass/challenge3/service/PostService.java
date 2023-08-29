package uol.compass.challenge3.service;

import java.util.ArrayList;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.queue.PostQueue;
import uol.compass.challenge3.queue.QueueType;
import uol.compass.challenge3.repository.PostRepository;

@Service
public class PostService {

    @PersistenceContext
    private EntityManager entityManager;
    private final PostRepository postRepository;
    private final PostQueue postQueue;

    public PostService(PostRepository postRepository, PostQueue postQueue) {
        this.postRepository = postRepository;
        this.postQueue = postQueue;
    }

    @Transactional
    public Post createPost(Long postId){

        if (postRepository.existsById(postId))
            throw new RuntimeException("existe");

        Post post = new Post(postId, null, null, null, new ArrayList<>());
        post.getStates().add(new State(Status.CREATED, post));
        Post createdPost = postRepository.save(post);

        postQueue.insertIntoQueue(QueueType.CREATED, post);

        return createdPost;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post update(Post post) {
        if (!postRepository.existsById(post.getId()))
            throw new RuntimeException();

        return postRepository.save(post);
    }

    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }

}

