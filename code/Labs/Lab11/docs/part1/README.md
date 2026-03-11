# Part 1

This folder maps to the first lab part:

- basic `Order` producer and consumer
- offset demo with `latest` and `earliest`
- competing consumers in one consumer group

Endpoints:

- `POST /api/scenarios/basic`
- `POST /api/scenarios/offsets/publish?batch=1`
- `POST /api/scenarios/offsets/start`
- `POST /api/scenarios/offsets/stop`
- `POST /api/scenarios/competing`

Implementation:

- `src/main/java/edu/miu/sa/lab11/kafka/controller/ScenarioController.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/KafkaScenarioService.java`
- `src/main/java/edu/miu/sa/lab11/kafka/service/OrderListenerService.java`
