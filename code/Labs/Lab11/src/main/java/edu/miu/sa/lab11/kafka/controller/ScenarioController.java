package edu.miu.sa.lab11.kafka.controller;

import edu.miu.sa.lab11.kafka.config.TopicNames;
import edu.miu.sa.lab11.kafka.domain.Order;
import edu.miu.sa.lab11.kafka.service.KafkaScenarioService;
import edu.miu.sa.lab11.kafka.service.ListenerLifecycleService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final KafkaScenarioService scenarioService;
    private final ListenerLifecycleService listenerLifecycleService;

    public ScenarioController(
            KafkaScenarioService scenarioService,
            ListenerLifecycleService listenerLifecycleService) {
        this.scenarioService = scenarioService;
        this.listenerLifecycleService = listenerLifecycleService;
    }

    @PostMapping("/basic")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse basic() {
        return response("basic", TopicNames.BASIC_ORDERS, "Published 5 basic orders",
                scenarioService.publishBasicOrders());
    }

    @PostMapping("/offsets/publish")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse offsetsPublish(@RequestParam(defaultValue = "1") int batch) {
        return response("offsets", TopicNames.OFFSET_ORDERS, "Published offset demo batch " + batch,
                scenarioService.publishOffsetOrders(batch));
    }

    @PostMapping("/offsets/start")
    public ScenarioResponse offsetsStart() {
        listenerLifecycleService.startOffsetListeners();
        return new ScenarioResponse("offsets", TopicNames.OFFSET_ORDERS,
                "Started latest and earliest offset listeners", List.of());
    }

    @PostMapping("/offsets/stop")
    public ScenarioResponse offsetsStop() {
        listenerLifecycleService.stopOffsetListeners();
        return new ScenarioResponse("offsets", TopicNames.OFFSET_ORDERS,
                "Stopped latest and earliest offset listeners", List.of());
    }

    @PostMapping("/competing")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse competing() {
        return response("competing-consumers", TopicNames.COMPETING_ORDERS,
                "Published competing consumer demo orders", scenarioService.publishCompetingOrders());
    }

    @PostMapping("/partitioned")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse partitioned(@RequestParam(defaultValue = "false") boolean uniqueKeys) {
        String message = uniqueKeys
                ? "Published partitioned demo orders using unique keys"
                : "Published partitioned demo orders using the same key";
        return response("partitioned", TopicNames.PARTITIONED_ORDERS, message,
                scenarioService.publishPartitionedOrders(uniqueKeys));
    }

    @PostMapping("/errors/blocking")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse blockingRetry() {
        return response("blocking-retry", TopicNames.BLOCKING_RETRY_ORDERS,
                "Published blocking retry demo orders", scenarioService.publishBlockingRetryOrders());
    }

    @PostMapping("/errors/retryable")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse retryable() {
        return response("retryable-topic", TopicNames.RETRYABLE_ORDERS,
                "Published retryable topic demo orders", scenarioService.publishRetryableOrders());
    }

    @PostMapping("/batching/producer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse producerBatching() throws InterruptedException {
        return response("producer-batching", TopicNames.PRODUCER_BATCH_ORDERS,
                "Published producer batching demo orders", scenarioService.publishProducerBatchOrders());
    }

    @PostMapping("/batching/consumer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse consumerBatching() throws InterruptedException {
        return response("consumer-batching", TopicNames.CONSUMER_BATCH_ORDERS,
                "Published consumer batching demo orders", scenarioService.publishConsumerBatchOrders());
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ScenarioResponse transactions(@RequestParam(required = false) Integer failAfter) {
        List<Order> orders = scenarioService.publishTransactionalOrders(failAfter);
        String message = failAfter == null
                ? "Published transactional demo orders"
                : "Published transactional demo orders and requested rollback after message " + failAfter;
        return response("transactions", TopicNames.TRANSACTIONAL_ORDERS, message, orders);
    }

    private ScenarioResponse response(String scenario, String topic, String message, List<Order> orders) {
        return new ScenarioResponse(
                scenario,
                topic,
                message,
                orders.stream().map(Order::orderNumber).toList()
        );
    }
}
