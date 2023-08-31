package uol.compass.challenge3.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import uol.compass.challenge3.entity.Post;

import java.util.Set;

@Component
public class PostIdValidator {

    private final Logger logger = LoggerFactory.getLogger(PostIdValidator.class);

    private final Validator validator;

    public PostIdValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateIdConstraints(Long id) {
        logger.info("Validating post id constraints");

        Post post = new Post();
        post.setId(id);

        Set<ConstraintViolation<Post>> violations = validator.validateProperty(post, "id");
        if (!violations.isEmpty()) {
            logger.error("Post id constraints violated");
            throw new ConstraintViolationException(violations);
        }
    }

}
