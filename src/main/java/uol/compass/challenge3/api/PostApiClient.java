package uol.compass.challenge3.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uol.compass.challenge3.entity.Comment;
import uol.compass.challenge3.entity.Post;

@FeignClient(name = "postsApiClient", url = "https://jsonplaceholder.typicode.com")
public interface PostApiClient {

    @GetMapping("/posts/{id}")
    Post findPostById(@PathVariable Long id);

    @GetMapping("/posts/{postId}/comments")
    List<Comment> findCommentsByPostId(@PathVariable Long postId);
}
