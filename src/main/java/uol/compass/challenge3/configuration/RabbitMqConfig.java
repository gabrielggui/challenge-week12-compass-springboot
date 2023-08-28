package uol.compass.challenge3.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue createdQueue() {
        return new Queue("CREATED");
    }

    @Bean
    public Queue postFindQueue() {
        return new Queue("POST_FIND");
    }

    @Bean
    public Queue postOkQueue() {
        return new Queue("POST_OK");
    }

    @Bean
    public Queue postCommentsFind() {
        return new Queue("COMMENTS_FIND");
    }

    @Bean
    public Queue postCommentsOk() {
        return new Queue("COMMENTS_OK");
    }

    @Bean
    public Queue postEnabled() {
        return new Queue("ENABLED");
    }

    @Bean
    public Queue postDisabled() {
        return new Queue("DISABLED");
    }

    @Bean
    public Queue postUpdating() {
        return new Queue("UPDATING");
    }

    @Bean
    public Queue postFailed() {
        return new Queue("FAILED");
    }
}
