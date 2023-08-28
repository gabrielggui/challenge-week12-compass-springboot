package uol.compass.challenge3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uol.compass.challenge3.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
