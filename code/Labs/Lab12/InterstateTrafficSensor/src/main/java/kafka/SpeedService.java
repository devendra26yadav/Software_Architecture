package kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SpeedService {

    private static final int SPEED_LIMIT = 72;

    private final SpeedCalculator speedCalculator;
    private final Sender sender;
    private final String tooFastTopic;

    public SpeedService(SpeedCalculator speedCalculator,
                        Sender sender,
                        @Value("${app.topic.tofasttopic}") String tooFastTopic) {
        this.speedCalculator = speedCalculator;
        this.sender = sender;
        this.tooFastTopic = tooFastTopic;
    }

    @KafkaListener(topics = {"cameratopic1", "cameratopic2"}, groupId = "speed-service")
    public void receive(SensorRecord sensorRecord) {
        SpeedRecord speedRecord = speedCalculator.handleRecord(sensorRecord);

        if (speedRecord == null) {
            return;
        }

        System.out.println("SpeedService -> " + speedRecord.getLicencePlate()
                + " speed = " + speedRecord.getSpeed());

        if (speedRecord.getSpeed() > SPEED_LIMIT) {
            sender.send(tooFastTopic, speedRecord);
        }
    }
}
