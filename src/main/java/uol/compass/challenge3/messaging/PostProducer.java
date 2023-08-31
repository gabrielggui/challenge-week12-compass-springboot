package uol.compass.challenge3.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import uol.compass.challenge3.entity.Post;

@Component
public class PostProducer {

    private final Logger logger = LoggerFactory.getLogger(PostProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public PostProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToQueue(Post post, QueueType queueType) {
        logger.info("Sending post to queue {}", queueType.name());

        try {
            rabbitTemplate.convertAndSend(queueType.name(), postToJson(post));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    public void sendToQueue(String jsonPost, QueueType queueType) {
        logger.info("Sending post to queue {}", queueType.name());

        rabbitTemplate.convertAndSend(queueType.name(), jsonPost);
    }

    private String postToJson(Post post) throws JsonProcessingException {
        logger.info("Converting post to JSON");

        return objectMapper.writeValueAsString(post);
    }

}