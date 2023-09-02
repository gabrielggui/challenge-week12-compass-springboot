package uol.compass.challenge3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uol.compass.challenge3.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.states s WHERE p.id = :id")
    Optional<Post> findById(Long id);

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.states s")
    List<Post> findAll();
}
