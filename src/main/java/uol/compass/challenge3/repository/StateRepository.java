package uol.compass.challenge3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uol.compass.challenge3.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {

    @Query("SELECT s FROM State s WHERE s.post.id = :postId AND s.date " +
            "= (SELECT MAX(ss.date) FROM State ss WHERE ss.post.id = :postId)")
    Optional<State> findLatestStateByPostId(Long postId);
}
