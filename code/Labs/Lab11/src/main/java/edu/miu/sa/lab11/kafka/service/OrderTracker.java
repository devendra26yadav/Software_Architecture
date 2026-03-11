package edu.miu.sa.lab11.kafka.service;

import edu.miu.sa.lab11.kafka.domain.Order;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class OrderTracker {

    private final Map<String, BlockingQueue<Order>> ordersByTopic = new ConcurrentHashMap<>();

    public void record(String topic, Order order) {
        ordersByTopic.computeIfAbsent(topic, ignored -> new LinkedBlockingQueue<>()).add(order);
    }

    public void clear(String topic) {
        ordersByTopic.computeIfAbsent(topic, ignored -> new LinkedBlockingQueue<>()).clear();
    }

    public List<Order> await(String topic, int expectedCount, Duration timeout) throws InterruptedException {
        BlockingQueue<Order> queue = ordersByTopic.computeIfAbsent(topic, ignored -> new LinkedBlockingQueue<>());
        List<Order> received = new ArrayList<>(expectedCount);
        long deadline = System.nanoTime() + timeout.toNanos();

        while (received.size() < expectedCount) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                break;
            }
            Order order = queue.poll(remaining, TimeUnit.NANOSECONDS);
            if (order != null) {
                received.add(order);
            }
        }

        return received;
    }
}
