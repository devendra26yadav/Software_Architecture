package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FeeCalculatorService {

    private final FeeCalculator feeCalculator;

    public FeeCalculatorService(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    @KafkaListener(topics = "${app.topic.ownerinfotopic}", groupId = "fee-service")
    public void receive(ViolationRecord violationRecord) {
        int fee = feeCalculator.calculateFee(violationRecord.getSpeed());

        System.out.println("FeeCalculatorService -> "
                + violationRecord.getLicencePlate()
                + ", " + violationRecord.getOwnerName()
                + ", speed = " + violationRecord.getSpeed()
                + ", fee = $" + fee);
    }
}
