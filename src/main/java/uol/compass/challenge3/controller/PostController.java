package uol.compass.challenge3.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> queryPosts(@RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {
        logger.info("Received request to query posts");

        if (page != null && size != null) {
            Page<Post> posts = postService.findAll(PageRequest.of(page, size));
            return new ResponseEntity<>(posts.getContent(), HttpStatus.OK);
        } else {
            List<Post> posts = postService.findAll();
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> queryPostById(@PathVariable Long id) {
        logger.info("Received request to query post by id {}", id);
        Post post = postService.findById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Post> processPost(@PathVariable Long id) {
        logger.info("Received request to process post by id {}", id);
        Post processedPost = postService.sendPostForProcessingByPostId(id);
        return new ResponseEntity<>(processedPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> reprocessPost(@PathVariable Long id) {
        logger.info("Received request to reprocess post by id {}", id);
        Post reprocessedPost = postService.sendPostForReprocessingByPostId(id);
        return new ResponseEntity<>(reprocessedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> disablePost(@PathVariable Long id) {
        logger.info("Received request to disable post by id {}", id);
        Post disabledPost = postService.disablePostById(id);
        return new ResponseEntity<>(disabledPost, HttpStatus.OK);
    }

}
