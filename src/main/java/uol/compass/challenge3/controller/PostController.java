package uol.compass.challenge3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.repository.PostRepository;
import uol.compass.challenge3.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

@Autowired
    private PostRepository postRepository;

    @GetMapping
    private Iterable<Post> queryPosts() {
        return postService.findAll();
    }

    @PostMapping("/{id}")
    public Post processPost(@PathVariable Long id) {
        return postService.createPost(id);
    }

    @GetMapping("/{id}")
    public Post g(@PathVariable Long id) {
        return postRepository.findById(id).get();
    }
}
