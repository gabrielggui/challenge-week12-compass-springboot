package uol.compass.challenge3.service;

import java.util.ArrayList;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.entity.State;
import uol.compass.challenge3.entity.Status;
import uol.compass.challenge3.repository.PostRepository;

@Service
public class PostService {

    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    @Autowired
    public PostService(RabbitTemplate rabbitTemplate, PostRepository postRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.postRepository = postRepository;
    }

    @Transactional
    public void createdPost(Long postId) {
        if (postRepository.existsById(postId))
            throw new RuntimeException();

        Post post = new Post(postId, "", "", null, new ArrayList<>());
        post.getStates().add(new State(Status.CREATED, post));
        postRepository.save(post);

        rabbitTemplate.convertAndSend("CREATED", postId);
    }

}
