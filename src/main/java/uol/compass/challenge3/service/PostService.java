package uol.compass.challenge3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.messaging.PostProducer;
import uol.compass.challenge3.messaging.QueueType;
import uol.compass.challenge3.repository.PostRepository;
import uol.compass.challenge3.repository.StateRepository;
import uol.compass.challenge3.utils.PostUtils;
import uol.compass.challenge3.validator.PostIdValidator;

@Service
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final PostProducer postProducer;
    private final StateRepository stateRepository;
    private final PostIdValidator postIdValidator;
    private final PostUtils postUtils;

    public PostService(PostRepository postRepository, PostProducer postProducer, StateRepository stateRepository,
            PostIdValidator postIdValidator, PostUtils postUtils) {
        this.postRepository = postRepository;
        this.postProducer = postProducer;
        this.stateRepository = stateRepository;
        this.postIdValidator = postIdValidator;
        this.postUtils = postUtils;
    }

    @CacheEvict(value = { "posts", "post" }, allEntries = true)
    public Post sendPostForProcessingByPostId(Long id) {
        logger.info("Received request to send post for processing by post id {}", id);
        postIdValidator.validateIdConstraints(id);

        Post post = new Post(id, "", "", null, new ArrayList<>());
        post = postUtils.updatePostState(post, Status.CREATED);
        Post savedPost = this.save(post);

        postProducer.sendToQueue(savedPost, QueueType.CREATED);
        logger.info("Successfully sent post for processing by post id {}", id);
        return post;
    }

    @CacheEvict(value = { "posts", "post" }, allEntries = true)
    public Post sendPostForReprocessingByPostId(Long id) {
        logger.info("Received request to send post for reprocessing by post id {}", id);
        Post post = this.findById(id);
        State lastPostState = stateRepository.findLatestStateByPostId(id).orElseThrow(NoSuchElementException::new);

        if (lastPostState.getStatus() == Status.ENABLED || lastPostState.getStatus() == Status.DISABLED) {
            post.setTitle(null);
            post.setBody(null);
            post.getComments().clear();
            post = postUtils.updatePostState(post, Status.UPDATING);
            Post updatedPost = this.update(post);

            postProducer.sendToQueue(post, QueueType.UPDATING);
            logger.info("Successfully sent post for reprocessing by post id {}", id);
            return updatedPost;
        } else {
            logger.error("Post is not in an enabled or disabled state");
            throw new IllegalStateException("\"Post\" is not in an enabled or disabled state.");
        }
    }

    @CacheEvict(value = { "posts", "post" }, allEntries = true)
    public Post disablePostById(Long id) {
        logger.info("Received request to disable post by post id {}", id);
        Post post = this.findById(id);
        State lastPostState = stateRepository.findLatestStateByPostId(id).orElseThrow(NoSuchElementException::new);

        if (lastPostState.getStatus() == Status.ENABLED) {
            post = postUtils.updatePostState(post, Status.DISABLED);
            post = this.update(post);

            return post;
        } else {
            logger.error("Post is not in an enabled state");
            throw new IllegalStateException("\"Post\" is not in an enabled state.");
        }
    }

    @CacheEvict(value = { "posts", "post" }, allEntries = true)
    public Post save(@Valid Post post) {
        logger.info("Received request to save post");

        if (postRepository.existsById(post.getId())) {
            logger.error("Post with id {} already exists", post.getId());
            throw new EntityExistsException("The entity \"post\" with the id entered already exists in the database.");
        }

        return postRepository.save(post);
    }

    @CacheEvict(value = { "posts", "post" }, allEntries = true)
    public Post update(@Valid Post post) {
        logger.info("Received request to update post");

        if (!postRepository.existsById(post.getId())) {
            logger.error("Post with id {} does not exist", post.getId());
            throw new EntityNotFoundException("The \"post\" entity with the id" +
                    " entered does not exist in the database.");
        }

        return postRepository.save(post);
    }

    @Cacheable("posts")
    public List<Post> findAll() {
        logger.info("Received request to find all posts");
        return postRepository.findAll();
    }

    public Page<Post> findAll(Pageable pageable) {
        logger.info("Received request to find all posts with pagination");
        return postRepository.findAll(pageable);
    }

    @Cacheable("post")
    public Post findById(Long id) {
        logger.info("Received request to find post by id {}", id);
        postIdValidator.validateIdConstraints(id);
        return postRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

}
