# Lab 12 Solution

This lab is organized into two parts.

## Part 1: Interstate Traffic Sensor

Path: `InterstateTrafficSensor/`

This is the Kafka-based stream processing solution for the interstate traffic sensor problem.

Implemented flow:

1. `Sensor` and `Sensor2` publish `SensorRecord` events to:
   - `cameratopic1`
   - `cameratopic2`
2. `SpeedService` consumes both camera topics, calculates speed, and publishes speeding cars to:
   - `tofasttopic`
3. `OwnerService` consumes `tofasttopic`, enriches the event with owner data, and publishes to:
   - `ownerinfotopic`
4. `FeeCalculatorService` consumes `ownerinfotopic` and prints:
   - license plate
   - owner info
   - speed
   - fee

Important classes:

- `kafka/SpeedService.java`
- `kafka/SpeedCalculator.java`
- `kafka/OwnerService.java`
- `kafka/FeeCalculatorService.java`
- `kafka/TopicConfiguration.java`

Tests:

- `src/test/java/kafka/SpeedCalculatorTest.java`
- `src/test/java/kafka/FeeCalculatorTest.java`

Run Part 1:

```bash
docker compose up -d
cd InterstateTrafficSensor
mvn test
mvn spring-boot:run
```

Kafka is configured through [docker-compose.yml]

Stop Kafka when finished:

```bash
docker compose down
```

## Part 2: Financial Risk System

Path: `Part2/`

This part is the architecture/design deliverable from the lab PDF.

Expected outputs:

1. Use case diagram
2. Activity diagram for generating a risk report
3. Context diagram
4. Container diagram
5. Component diagram
6. Sequence diagram
