# Part 3

This folder maps to the retry and dead-letter topic exercises:

- blocking retries with `DefaultErrorHandler`
- non-blocking retries with `@RetryableTopic`
- DLT listeners for both flows

Endpoints:

- `POST /api/scenarios/errors/blocking`
- `POST /api/scenarios/errors/retryable`

Implementation:

- `src/main/java/edu/miu/sa/lab11/kafka/config/KafkaInfrastructureConfig.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderListenerService.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderFactory.java`
