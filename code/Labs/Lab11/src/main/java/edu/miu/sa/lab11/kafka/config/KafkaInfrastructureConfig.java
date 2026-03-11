package edu.miu.sa.lab11.kafka.config;

import edu.miu.sa.lab11.kafka.domain.Order;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaInfrastructureConfig {

    @Bean
    ProducerFactory<String, Order> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean(name = {"kafkaTemplate", "defaultRetryTopicKafkaTemplate"})
    KafkaTemplate<String, Order> kafkaTemplate(ProducerFactory<String, Order> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ProducerFactory<String, Order> transactionalProducerFactory(
            KafkaProperties kafkaProperties,
            @Value("${app.kafka.transaction-id-prefix:tx-order-}") String transactionIdPrefix) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        DefaultKafkaProducerFactory<String, Order> factory = new DefaultKafkaProducerFactory<>(config);
        if (transactionIdPrefix != null && !transactionIdPrefix.isBlank()) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    @Bean
    KafkaTemplate<String, Order> transactionalKafkaTemplate(
            ProducerFactory<String, Order> transactionalProducerFactory) {
        return new KafkaTemplate<>(transactionalProducerFactory);
    }

    @Bean
    ConsumerFactory<String, Order> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "edu.miu.sa.lab11.kafka");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Order.class.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Order> kafkaListenerContainerFactory(
            @Qualifier("consumerFactory") ConsumerFactory<String, Order> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    DefaultErrorHandler blockingRetryErrorHandler(KafkaTemplate<String, Order> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition())
        );
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Order> blockingRetryKafkaListenerContainerFactory(
            @Qualifier("consumerFactory") ConsumerFactory<String, Order> consumerFactory,
            DefaultErrorHandler blockingRetryErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(blockingRetryErrorHandler);
        return factory;
    }

    @Bean
    ConsumerFactory<String, Order> batchConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "edu.miu.sa.lab11.kafka");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Order.class.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 3000);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Order> batchKafkaListenerContainerFactory(
            @Qualifier("batchConsumerFactory") ConsumerFactory<String, Order> batchConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchConsumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(3000L);
        return factory;
    }

    @Bean
    ConsumerFactory<String, Order> readCommittedConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "edu.miu.sa.lab11.kafka");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Order.class.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Order> readCommittedKafkaListenerContainerFactory(
            @Qualifier("readCommittedConsumerFactory") ConsumerFactory<String, Order> readCommittedConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(readCommittedConsumerFactory);
        return factory;
    }

    @Bean
    ProducerFactory<String, Order> batchingProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 6000);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 32_768);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, Order> batchingKafkaTemplate(
            ProducerFactory<String, Order> batchingProducerFactory) {
        return new KafkaTemplate<>(batchingProducerFactory);
    }

    @Bean
    TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("retry-topic-");
        scheduler.initialize();
        return scheduler;
    }
}
