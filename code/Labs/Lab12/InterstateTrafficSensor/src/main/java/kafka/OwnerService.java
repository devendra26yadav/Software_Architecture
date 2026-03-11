package kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerDirectory ownerDirectory;
    private final Sender sender;
    private final String ownerInfoTopic;

    public OwnerService(OwnerDirectory ownerDirectory,
                        Sender sender,
                        @Value("${app.topic.ownerinfotopic}") String ownerInfoTopic) {
        this.ownerDirectory = ownerDirectory;
        this.sender = sender;
        this.ownerInfoTopic = ownerInfoTopic;
    }

    @KafkaListener(topics = "${app.topic.tofasttopic}", groupId = "owner-service")
    public void receive(SpeedRecord speedRecord) {
        OwnerInfo ownerInfo = ownerDirectory.findOwner(speedRecord.getLicencePlate());

        System.out.println("OwnerService -> " + speedRecord.getLicencePlate()
                + " owner = " + ownerInfo.getOwnerName());

        ViolationRecord violationRecord = new ViolationRecord(
                speedRecord.getLicencePlate(),
                speedRecord.getSpeed(),
                ownerInfo.getOwnerName(),
                ownerInfo.getOwnerAddress());

        sender.send(ownerInfoTopic, violationRecord);
    }
}
