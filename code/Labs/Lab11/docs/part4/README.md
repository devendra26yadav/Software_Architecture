# Part 4

This folder maps to the batching exercises:

- producer-side batching with `linger.ms=6000`
- consumer-side batch polling

Endpoints:

- `POST /api/scenarios/batching/producer`
- `POST /api/scenarios/batching/consumer`

Implementation:

- `src/main/java/edu/miu/sa/lab11/kafka/config/KafkaInfrastructureConfig.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/KafkaScenarioService.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderListenerService.java`
