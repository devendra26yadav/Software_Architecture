package edu.miu.sa.lab11.kafka.service;

import edu.miu.sa.lab11.kafka.config.TopicNames;
import edu.miu.sa.lab11.kafka.domain.Order;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaScenarioService {

    private static final Logger log = LoggerFactory.getLogger(KafkaScenarioService.class);

    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final KafkaTemplate<String, Order> batchingKafkaTemplate;
    private final KafkaTemplate<String, Order> transactionalKafkaTemplate;
    private final OrderFactory orderFactory;

    public KafkaScenarioService(
            KafkaTemplate<String, Order> kafkaTemplate,
            @Qualifier("batchingKafkaTemplate") KafkaTemplate<String, Order> batchingKafkaTemplate,
            @Qualifier("transactionalKafkaTemplate") KafkaTemplate<String, Order> transactionalKafkaTemplate,
            OrderFactory orderFactory) {
        this.kafkaTemplate = kafkaTemplate;
        this.batchingKafkaTemplate = batchingKafkaTemplate;
        this.transactionalKafkaTemplate = transactionalKafkaTemplate;
        this.orderFactory = orderFactory;
    }

    public List<Order> publishBasicOrders() {
        List<Order> orders = orderFactory.basicOrders();
        sendOrders(TopicNames.BASIC_ORDERS, orders, true);
        return orders;
    }

    public List<Order> publishOffsetOrders(int batchNumber) {
        List<Order> orders = orderFactory.offsetOrders(batchNumber);
        sendOrders(TopicNames.OFFSET_ORDERS, orders, true);
        return orders;
    }

    public List<Order> publishCompetingOrders() {
        List<Order> orders = orderFactory.competingOrders();
        sendOrders(TopicNames.COMPETING_ORDERS, orders, true);
        return orders;
    }

    public List<Order> publishPartitionedOrders(boolean uniqueKeys) {
        List<Order> orders = orderFactory.partitionedOrders();
        for (Order order : orders) {
            String key = uniqueKeys ? order.orderNumber() : "same-key";
            kafkaTemplate.send(TopicNames.PARTITIONED_ORDERS, key, order);
            log.info("Published partition demo order={} key={} topic={}",
                    order.orderNumber(), key, TopicNames.PARTITIONED_ORDERS);
        }
        return orders;
    }

    public List<Order> publishBlockingRetryOrders() {
        List<Order> orders = orderFactory.blockingRetryOrders();
        sendOrders(TopicNames.BLOCKING_RETRY_ORDERS, orders, true);
        return orders;
    }

    public List<Order> publishRetryableOrders() {
        List<Order> orders = orderFactory.retryableOrders();
        sendOrders(TopicNames.RETRYABLE_ORDERS, orders, true);
        return orders;
    }

    public List<Order> publishProducerBatchOrders() throws InterruptedException {
        List<Order> orders = orderFactory.batchOrders("PB");
        for (Order order : orders) {
            batchingKafkaTemplate.send(TopicNames.PRODUCER_BATCH_ORDERS, order.orderNumber(), order);
            log.info("Producer batching demo sent order={} at {}", order.orderNumber(), LocalTime.now());
            Thread.sleep(2000);
        }
        return orders;
    }

    public List<Order> publishConsumerBatchOrders() {
        List<Order> orders = orderFactory.batchOrders("CB");
        for (Order order : orders) {
            kafkaTemplate.send(TopicNames.CONSUMER_BATCH_ORDERS, order.orderNumber(), order);
            log.info("Consumer batching demo sent order={} at {}", order.orderNumber(), LocalTime.now());
        }
        kafkaTemplate.flush();
        return orders;
    }

    public List<Order> publishTransactionalOrders(Integer failAfter) {
        List<Order> orders = orderFactory.transactionalOrders();
        return transactionalKafkaTemplate.executeInTransaction(operations -> {
            for (int index = 0; index < orders.size(); index++) {
                Order order = orders.get(index);
                operations.send(TopicNames.TRANSACTIONAL_ORDERS, order.orderNumber(), order);
                log.info("Transactional demo sent order={} at {}", order.orderNumber(), LocalTime.now());
                if (failAfter != null && index + 1 == failAfter) {
                    throw new IllegalStateException("Forced transactional rollback after order " + order.orderNumber());
                }
            }
            return orders;
        });
    }

    private void sendOrders(String topic, List<Order> orders, boolean useOrderNumberAsKey) {
        for (Order order : orders) {
            String key = useOrderNumberAsKey ? order.orderNumber() : null;
            kafkaTemplate.send(topic, key, order);
            log.info("Published order={} topic={} key={}", order.orderNumber(), topic, key);
        }
    }
}
