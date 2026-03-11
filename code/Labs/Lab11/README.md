# Lab 11 Kafka

## Status

The lab is complete for submission:

- Parts 1 to 6 are implemented in code
- Part 7 diagrams PNG
- The app was verified against the Docker Kafka broker on `localhost:9092`
- `mvn test` passes


## Part folders

The lab is organized under `docs/part1` through `docs/part7`.

- `docs/part1`: basic producer/consumer, offsets, competing consumers
- `docs/part2`: partitioning
- `docs/part3`: retries and DLT
- `docs/part4`: batching
- `docs/part5`: tests
- `docs/part6`: transactions
- `docs/part7`: sequence diagram PNG

## What is implemented

- Basic producer and consumer for `Order` objects
- Offset demo with `latest` and `earliest` consumers
- Competing consumers in the same group
- Three-partition topic with one listener per partition
- Blocking retries with `DefaultErrorHandler` and a `.DLT` topic
- Non-blocking retries with `@RetryableTopic` and DLT handling
- Producer-side batching and consumer-side batch polling
- Transactional send of five orders with a `read_committed` consumer
- Integration tests using Embedded Kafka and Testcontainers

## Run Kafka

The lesson uses a local Kafka broker:

```bash
docker run -d --name kafka -p 9092:9092 apache/kafka
```

## Run the application

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

## Scenario endpoints

Each endpoint publishes a ready-made set of `Order` messages to the corresponding lesson topic.

```bash
curl -X POST http://localhost:8080/api/scenarios/basic
curl -X POST http://localhost:8080/api/scenarios/competing
curl -X POST "http://localhost:8080/api/scenarios/partitioned?uniqueKeys=false"
curl -X POST "http://localhost:8080/api/scenarios/partitioned?uniqueKeys=true"
curl -X POST http://localhost:8080/api/scenarios/errors/blocking
curl -X POST http://localhost:8080/api/scenarios/errors/retryable
curl -X POST http://localhost:8080/api/scenarios/batching/producer
curl -X POST http://localhost:8080/api/scenarios/batching/consumer
curl -X POST http://localhost:8080/api/scenarios/transactions
curl -X POST "http://localhost:8080/api/scenarios/transactions?failAfter=3"
```

## Offset demo sequence

The lesson's `latest` versus `earliest` behavior only makes sense when messages already exist before the listeners start. Use this sequence:

```bash
curl -X POST "http://localhost:8080/api/scenarios/offsets/publish?batch=1"
curl -X POST http://localhost:8080/api/scenarios/offsets/start
curl -X POST "http://localhost:8080/api/scenarios/offsets/publish?batch=2"
```

Expected behavior:

- The `earliest` listener consumes the first batch after it starts.
- The `latest` listener skips the first batch and only consumes the second batch.

Stop those demo listeners with:

```bash
curl -X POST http://localhost:8080/api/scenarios/offsets/stop
```

## Tests

```bash
mvn test
```
