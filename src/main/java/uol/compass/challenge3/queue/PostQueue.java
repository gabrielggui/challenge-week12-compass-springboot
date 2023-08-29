package uol.compass.challenge3.queue;

import org.springframework.stereotype.Component;
import uol.compass.challenge3.entity.Post;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PostQueue {

    private final Map<String, BlockingQueue<Post>> postQueues;

    public PostQueue(Map<String, BlockingQueue<Post>> postQueues) {
        this.postQueues = postQueues;
        Arrays.stream(QueueType.values()).forEach(q -> postQueues.put(q.name(), new LinkedBlockingQueue<>()));
    }

    public Map<String, BlockingQueue<Post>> findAllQueues() {
        return postQueues;
    }

    public BlockingQueue<Post> findQueueByType(QueueType queueType) {
        return postQueues.get(queueType.name());
    }

    public void insertIntoQueue(QueueType queueType, Post post) {
        try {
            postQueues.get(queueType.name()).put(post);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
