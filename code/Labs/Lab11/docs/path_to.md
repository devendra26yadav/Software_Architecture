# Submission Checklist

## Before demo

1. Start Kafka:

```bash
docker start kafka
```

If the container does not exist yet:

```bash
docker run -d --name kafka -p 9092:9092 apache/kafka
```

2. Start the app:

```bash
mvn spring-boot:run
```

3. Open the project docs:

- `docs/part1` to `docs/part7`
- `docs/part7/part7-startup-sequence.png`
- `docs/part7/part7-place-order-sequence.png`

## What to demo

### Part 1

```bash
curl -X POST http://localhost:8080/api/scenarios/basic
curl -X POST "http://localhost:8080/api/scenarios/offsets/publish?batch=1"
curl -X POST http://localhost:8080/api/scenarios/offsets/start
curl -X POST "http://localhost:8080/api/scenarios/offsets/publish?batch=2"
curl -X POST http://localhost:8080/api/scenarios/offsets/stop
curl -X POST http://localhost:8080/api/scenarios/competing
```

Expected:

- basic consumer receives 5 `Order` messages
- `earliest` consumes batch 1 and batch 2
- `latest` skips batch 1 and consumes only batch 2
- competing consumers share one group; with one partition, one listener gets the records

### Part 2

```bash
curl -X POST "http://localhost:8080/api/scenarios/partitioned?uniqueKeys=false"
curl -X POST "http://localhost:8080/api/scenarios/partitioned?uniqueKeys=true"
```

Expected:

- same key keeps all records on one partition
- unique keys distribute records across partitions

### Part 3

```bash
curl -X POST http://localhost:8080/api/scenarios/errors/blocking
curl -X POST http://localhost:8080/api/scenarios/errors/retryable
```

Expected:

- blocking retry retries the rejected order and then sends it to `.DLT`
- retryable topic retries the rejected order and then sends it to `orders.error.retryable-dlt`

### Part 4

```bash
curl -X POST http://localhost:8080/api/scenarios/batching/producer
curl -X POST http://localhost:8080/api/scenarios/batching/consumer
```

Expected:

- producer batching delivers records in grouped bursts
- consumer batching logs `consumer-batch polled=6`

### Part 5

```bash
mvn test
```

Expected:

- embedded Kafka test passes
- Testcontainers test is present but may be skipped on this machine due to Docker Desktop detection in Testcontainers

### Part 6

```bash
curl -X POST http://localhost:8080/api/scenarios/transactions
curl -X POST "http://localhost:8080/api/scenarios/transactions?failAfter=3"
```

Expected:

- normal transaction commits all 5 messages
- rollback request returns `500` and no new committed transactional records appear

### Part 7

Open:

- `docs/part7/part7-startup-sequence.png`
- `docs/part7/part7-place-order-sequence.png`

## Final verification

```bash
mvn test
```

The implementation is complete for lab submission. The only known environment caveat is the Testcontainers Docker validation issue.
