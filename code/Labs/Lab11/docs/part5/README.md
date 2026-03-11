# Part 5

This folder maps to the testing exercises:

- embedded Kafka integration test
- Testcontainers-based integration test

Test files:

- `src/test/java/edu/miu/sa/lab11/kafka/EmbeddedKafkaOrderIntegrationTest.java`
- `src/test/java/edu/miu/sa/lab11/kafka/TestcontainersOrderIntegrationTest.java`

Run:

- `mvn test`

Note:

- the embedded Kafka test passes
- the Testcontainers test is present, but may be skipped on this machine because Testcontainers cannot validate Docker Desktop
