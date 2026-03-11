package edu.miu.sa.lab11.kafka.service;

import edu.miu.sa.lab11.kafka.config.TopicNames;
import edu.miu.sa.lab11.kafka.domain.Order;
import edu.miu.sa.lab11.kafka.domain.OrderStatus;
import java.time.LocalTime;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class OrderListenerService {

    private static final Logger log = LoggerFactory.getLogger(OrderListenerService.class);

    private final OrderTracker orderTracker;

    public OrderListenerService(OrderTracker orderTracker) {
        this.orderTracker = orderTracker;
    }

    @KafkaListener(topics = TopicNames.BASIC_ORDERS, groupId = "basic-orders-group")
    public void consumeBasic(Order order,
                             @Header(KafkaHeaders.OFFSET) long offset,
                             @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("basic group=basic-orders-group order={} offset={} partition={}",
                order.orderNumber(), offset, partition);
        orderTracker.record(TopicNames.BASIC_ORDERS, order);
    }

    @KafkaListener(
            id = ListenerLifecycleService.OFFSET_LATEST_LISTENER,
            topics = TopicNames.OFFSET_ORDERS,
            groupId = "offset-latest-group",
            autoStartup = "false",
            properties = {"auto.offset.reset=latest"}
    )
    public void consumeLatestOffset(Order order, @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("offset-latest group=offset-latest-group order={} offset={}", order.orderNumber(), offset);
    }

    @KafkaListener(
            id = ListenerLifecycleService.OFFSET_EARLIEST_LISTENER,
            topics = TopicNames.OFFSET_ORDERS,
            groupId = "offset-earliest-group",
            autoStartup = "false",
            properties = {"auto.offset.reset=earliest"}
    )
    public void consumeEarliestOffset(Order order, @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("offset-earliest group=offset-earliest-group order={} offset={}", order.orderNumber(), offset);
    }

    @KafkaListener(topics = TopicNames.COMPETING_ORDERS, groupId = "orders-workers-group")
    public void consumeCompetingOne(Order order,
                                    @Header(KafkaHeaders.OFFSET) long offset,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("competing-listener-1 group=orders-workers-group order={} offset={} partition={}",
                order.orderNumber(), offset, partition);
    }

    @KafkaListener(topics = TopicNames.COMPETING_ORDERS, groupId = "orders-workers-group")
    public void consumeCompetingTwo(Order order,
                                    @Header(KafkaHeaders.OFFSET) long offset,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("competing-listener-2 group=orders-workers-group order={} offset={} partition={}",
                order.orderNumber(), offset, partition);
    }

    @KafkaListener(
            topics = TopicNames.PARTITIONED_ORDERS,
            groupId = "partition-demo-group",
            topicPartitions = @TopicPartition(topic = TopicNames.PARTITIONED_ORDERS, partitions = {"0"})
    )
    public void consumePartition0(Order order,
                                  @Header(KafkaHeaders.OFFSET) long offset,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("partition-listener-0 order={} offset={} partition={}", order.orderNumber(), offset, partition);
    }

    @KafkaListener(
            topics = TopicNames.PARTITIONED_ORDERS,
            groupId = "partition-demo-group",
            topicPartitions = @TopicPartition(topic = TopicNames.PARTITIONED_ORDERS, partitions = {"1"})
    )
    public void consumePartition1(Order order,
                                  @Header(KafkaHeaders.OFFSET) long offset,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("partition-listener-1 order={} offset={} partition={}", order.orderNumber(), offset, partition);
    }

    @KafkaListener(
            topics = TopicNames.PARTITIONED_ORDERS,
            groupId = "partition-demo-group",
            topicPartitions = @TopicPartition(topic = TopicNames.PARTITIONED_ORDERS, partitions = {"2"})
    )
    public void consumePartition2(Order order,
                                  @Header(KafkaHeaders.OFFSET) long offset,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("partition-listener-2 order={} offset={} partition={}", order.orderNumber(), offset, partition);
    }

    @KafkaListener(
            topics = TopicNames.BLOCKING_RETRY_ORDERS,
            groupId = "blocking-retry-group",
            containerFactory = "blockingRetryKafkaListenerContainerFactory"
    )
    public void consumeWithBlockingRetry(Order order,
                                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                         @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("blocking-retry order={} topic={} offset={}", order.orderNumber(), topic, offset);
        if (order.status() == OrderStatus.REJECTED) {
            throw new IllegalStateException("Rejected order " + order.orderNumber());
        }
    }

    @KafkaListener(topics = TopicNames.BLOCKING_RETRY_DLT, groupId = "blocking-retry-dlt-group")
    public void consumeBlockingRetryDlt(Order order) {
        log.warn("blocking-retry-dlt received order={}", order.orderNumber());
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, multiplier = 2))
    @KafkaListener(topics = TopicNames.RETRYABLE_ORDERS, groupId = "retryable-topic-group")
    public void consumeWithRetryableTopic(Order order,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("retryable-topic order={} topic={} offset={}", order.orderNumber(), topic, offset);
        if (order.status() == OrderStatus.REJECTED) {
            throw new IllegalStateException("Rejected order " + order.orderNumber());
        }
    }

    @DltHandler
    public void listenRetryableDlt(Order order,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   @Header(KafkaHeaders.OFFSET) long offset) {
        log.warn("retryable-dlt order={} topic={} offset={}", order.orderNumber(), topic, offset);
    }

    @KafkaListener(topics = TopicNames.PRODUCER_BATCH_ORDERS, groupId = "producer-batch-group")
    public void consumeProducerBatch(Order order) {
        log.info("producer-batch-consumer order={} receivedAt={}", order.orderNumber(), LocalTime.now());
    }

    @KafkaListener(
            topics = TopicNames.CONSUMER_BATCH_ORDERS,
            groupId = "consumer-batch-group",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    public void consumeConsumerBatch(List<ConsumerRecord<String, Order>> records) {
        log.info("consumer-batch polled={} receivedAt={}", records.size(), LocalTime.now());
        for (ConsumerRecord<String, Order> record : records) {
            log.info("consumer-batch order={} offset={} partition={}",
                    record.value().orderNumber(), record.offset(), record.partition());
        }
    }

    @KafkaListener(
            topics = TopicNames.TRANSACTIONAL_ORDERS,
            groupId = "transactional-orders-group",
            containerFactory = "readCommittedKafkaListenerContainerFactory"
    )
    public void consumeCommittedTransaction(Order order,
                                            @Header(KafkaHeaders.OFFSET) long offset,
                                            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("transactional-consumer order={} offset={} partition={} receivedAt={}",
                order.orderNumber(), offset, partition, LocalTime.now());
        orderTracker.record(TopicNames.TRANSACTIONAL_ORDERS, order);
    }
}
