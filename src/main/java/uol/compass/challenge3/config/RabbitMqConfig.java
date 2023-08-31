package uol.compass.challenge3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue createdQueue() {
        logger.info("Generating \"CREATED\" queue");
        return new Queue("CREATED");
    }

    @Bean
    public Queue postUpdating() {
        logger.info("Generating \"UPDATING\" queue");
        return new Queue("UPDATING");
    }

}