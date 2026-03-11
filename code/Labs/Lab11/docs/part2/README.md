# Part 2

This folder maps to the partitioning exercises:

- send all orders with the same key to a three-partition topic
- send orders with unique keys and observe distribution across partitions

Endpoints:

- `POST /api/scenarios/partitioned?uniqueKeys=false`
- `POST /api/scenarios/partitioned?uniqueKeys=true`

Implementation:

- `src/main/java/edu/miu/sa/lab11/kafka/config/KafkaTopicConfig.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/KafkaScenarioService.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderListenerService.java`
