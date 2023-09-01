package uol.compass.challenge3.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import uol.compass.challenge3.entity.Post;
import uol.compass.challenge3.utils.PostUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostProducer {

    private final Logger logger = LoggerFactory.getLogger(PostProducer.class);

    private final JmsTemplate jmsTemplate;
    private final PostUtils postUtils;

    public PostProducer(JmsTemplate jmsTemplate, PostUtils postUtils) {
        this.jmsTemplate = jmsTemplate;
        this.postUtils = postUtils;
    }

    public void sendToQueue(Post post, QueueType queueType) {
        logger.info("Sending post to queue {}", queueType.name());

        try {
            jmsTemplate.convertAndSend(queueType.name(), postUtils.postToJson(post));
        } catch (JmsException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendToQueue(String jsonPost, QueueType queueType) {
        logger.info("Sending post to queue {}", queueType.name());

        jmsTemplate.convertAndSend(queueType.name(), jsonPost);
    }

}