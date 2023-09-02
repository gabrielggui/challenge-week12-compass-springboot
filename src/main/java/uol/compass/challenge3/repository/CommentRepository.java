package uol.compass.challenge3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uol.compass.challenge3.entity.Comment;
import uol.compass.challenge3.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteByPost(Post post);

}
