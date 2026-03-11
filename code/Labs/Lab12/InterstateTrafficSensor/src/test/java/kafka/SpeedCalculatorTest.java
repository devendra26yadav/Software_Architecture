package kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SpeedCalculatorTest {

    private final SpeedCalculator speedCalculator = new SpeedCalculator();

    @Test
    void returnsEmptyUntilMatchingCameraRecordArrives() {
        SpeedRecord result = speedCalculator.handleRecord(new SensorRecord("AA1000", 10, 5, 1));

        assertNull(result);
    }

    @Test
    void computesSpeedWhenCameraTwoArrivesAfterCameraOne() {
        speedCalculator.handleRecord(new SensorRecord("AA1000", 10, 5, 1));

        SpeedRecord speedRecord = speedCalculator.handleRecord(new SensorRecord("AA1000", 10, 25, 2));

        assertEquals("AA1000", speedRecord.getLicencePlate());
        assertEquals(20, speedRecord.getTravelTimeInSeconds());
        assertEquals(90, speedRecord.getSpeed());
    }

    @Test
    void computesSpeedEvenIfKafkaDeliversCameraTwoBeforeCameraOne() {
        speedCalculator.handleRecord(new SensorRecord("AA1000", 10, 25, 2));

        SpeedRecord speedRecord = speedCalculator.handleRecord(new SensorRecord("AA1000", 10, 5, 1));

        assertEquals(20, speedRecord.getTravelTimeInSeconds());
        assertEquals(90, speedRecord.getSpeed());
    }
}
