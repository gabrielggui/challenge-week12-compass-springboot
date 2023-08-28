package uol.compass.challenge3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import uol.compass.challenge3.service.PostService;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts/{id}")
    public String processPost(@PathVariable Long id) {
        postService.createdPost(id);
        return "";
    }
}
