# Part 6

This folder maps to the transactional producer and `read_committed` consumer exercise.

Endpoints:

- `POST /api/scenarios/transactions`
- `POST /api/scenarios/transactions?failAfter=3`

Implementation:

- `src/main/java/edu/miu/sa/lab11/kafka/config/KafkaInfrastructureConfig.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/KafkaScenarioService.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderListenerService.java`

Behavior:

- success case commits all 5 orders
- rollback case aborts the transaction and the consumer receives no newly committed records
