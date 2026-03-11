package edu.miu.sa.lab11.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import edu.miu.sa.lab11.kafka.config.TopicNames;
import edu.miu.sa.lab11.kafka.domain.Order;
import edu.miu.sa.lab11.kafka.service.KafkaScenarioService;
import edu.miu.sa.lab11.kafka.service.OrderTracker;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@DirtiesContext
class TestcontainersOrderIntegrationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0"));

    @DynamicPropertySource
    static void configureKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaScenarioService scenarioService;

    @Autowired
    private OrderTracker orderTracker;

    @Test
    void basicProducerAndConsumerWorkWithTestcontainersKafka() throws Exception {
        orderTracker.clear(TopicNames.BASIC_ORDERS);

        List<Order> sentOrders = scenarioService.publishBasicOrders();
        List<Order> receivedOrders = orderTracker.await(TopicNames.BASIC_ORDERS, sentOrders.size(), Duration.ofSeconds(10));

        assertThat(receivedOrders)
                .extracting(Order::orderNumber)
                .containsExactlyInAnyOrderElementsOf(sentOrders.stream().map(Order::orderNumber).toList());
    }
}
