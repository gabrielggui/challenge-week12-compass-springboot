package uol.compass.challenge3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import uol.compass.challenge3.messaging.QueueType;

@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final Logger logger = LoggerFactory.getLogger(ApplicationShutdownListener.class);

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public ApplicationShutdownListener(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Application shutting down. Purging RabbitMQ queues...");

        for (QueueType queueType : QueueType.values()) {
            String queueName = queueType.name();
            rabbitAdmin.purgeQueue(queueName, false);
            logger.info("Purged queue: {}", queueName);
        }

        logger.info("RabbitMQ queue purge completed.");
    }

}
