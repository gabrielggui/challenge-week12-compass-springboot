package uol.compass.challenge3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public Queue createdQueue() {
        logger.info("Generating \"CREATED\" queue");
        return new Queue("CREATED");
    }

    @Bean
    public Queue postFindQueue() {
        logger.info("Generating \"POST_FIND\" queue");
        return new Queue("POST_FIND");
    }

    @Bean
    public Queue postOkQueue() {
        logger.info("Generating \"POST_OK\" queue");
        return new Queue("POST_OK");
    }

    @Bean
    public Queue postCommentsFind() {
        logger.info("Generating \"COMMENTS_FIND\" queue");
        return new Queue("COMMENTS_FIND");
    }

    @Bean
    public Queue postCommentsOk() {
        logger.info("Generating \"COMMENTS_OK\" queue");
        return new Queue("COMMENTS_OK");
    }

    @Bean
    public Queue postEnabled() {
        logger.info("Generating \"ENABLED\" queue");
        return new Queue("ENABLED");
    }

    @Bean
    public Queue postDisabled() {
        logger.info("Generating \"DISABLED\" queue");
        return new Queue("DISABLED");
    }

    @Bean
    public Queue postUpdating() {
        logger.info("Generating \"UPDATING\" queue");
        return new Queue("UPDATING");
    }

    @Bean
    public Queue postFailed() {
        logger.info("Generating \"FAILED\" queue");
        return new Queue("FAILED");
    }
}