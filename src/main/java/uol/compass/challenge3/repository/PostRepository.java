package uol.compass.challenge3.repository;

import org.springframework.data.repository.CrudRepository;

import uol.compass.challenge3.entity.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

}
