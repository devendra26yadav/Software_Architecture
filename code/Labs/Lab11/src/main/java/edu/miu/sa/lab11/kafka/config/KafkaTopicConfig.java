package edu.miu.sa.lab11.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic basicOrdersTopic() {
        return TopicBuilder.name(TopicNames.BASIC_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic offsetOrdersTopic() {
        return TopicBuilder.name(TopicNames.OFFSET_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic competingOrdersTopic() {
        return TopicBuilder.name(TopicNames.COMPETING_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic partitionedOrdersTopic() {
        return TopicBuilder.name(TopicNames.PARTITIONED_ORDERS).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic blockingRetryOrdersTopic() {
        return TopicBuilder.name(TopicNames.BLOCKING_RETRY_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic blockingRetryDltTopic() {
        return TopicBuilder.name(TopicNames.BLOCKING_RETRY_DLT).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic retryableOrdersTopic() {
        return TopicBuilder.name(TopicNames.RETRYABLE_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic producerBatchOrdersTopic() {
        return TopicBuilder.name(TopicNames.PRODUCER_BATCH_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic consumerBatchOrdersTopic() {
        return TopicBuilder.name(TopicNames.CONSUMER_BATCH_ORDERS).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic transactionalOrdersTopic() {
        return TopicBuilder.name(TopicNames.TRANSACTIONAL_ORDERS).partitions(1).replicas(1).build();
    }
}
