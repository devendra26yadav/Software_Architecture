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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
@EmbeddedKafka(partitions = 1, topics = {TopicNames.BASIC_ORDERS})
@DirtiesContext
class EmbeddedKafkaOrderIntegrationTest {

    @Autowired
    private KafkaScenarioService scenarioService;

    @Autowired
    private OrderTracker orderTracker;

    @Test
    void basicProducerAndConsumerWorkWithEmbeddedKafka() throws Exception {
        orderTracker.clear(TopicNames.BASIC_ORDERS);

        List<Order> sentOrders = scenarioService.publishBasicOrders();
        List<Order> receivedOrders = orderTracker.await(TopicNames.BASIC_ORDERS, sentOrders.size(), Duration.ofSeconds(10));

        assertThat(receivedOrders)
                .extracting(Order::orderNumber)
                .containsExactlyInAnyOrderElementsOf(sentOrders.stream().map(Order::orderNumber).toList());
    }
}
