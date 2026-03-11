package kafka;

import org.springframework.stereotype.Component;

@Component
public class FeeCalculator {

    public int calculateFee(int speed) {
        if (speed <= 72) {
            return 0;
        }
        if (speed <= 77) {
            return 25;
        }
        if (speed <= 82) {
            return 45;
        }
        if (speed <= 90) {
            return 80;
        }
        return 125;
    }
}
