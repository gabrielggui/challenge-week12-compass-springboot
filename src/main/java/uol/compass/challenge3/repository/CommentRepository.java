package uol.compass.challenge3.repository;


import org.springframework.data.repository.CrudRepository;

import uol.compass.challenge3.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
