package kafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SpeedCalculator {

	private Map<String, SensorRecord> recordStream = new HashMap<String, SensorRecord>();

	public SpeedRecord handleRecord(SensorRecord sensorRecord) {
		SensorRecord oldRecord = recordStream.get(sensorRecord.getLicencePlate());

		if (oldRecord == null) {
			recordStream.put(sensorRecord.getLicencePlate(), sensorRecord);
			return null;
		}

		if (oldRecord.getCameraId() == sensorRecord.getCameraId()) {
			recordStream.put(sensorRecord.getLicencePlate(), sensorRecord);
			return null;
		}

		int time = calculateTime(oldRecord, sensorRecord);
		recordStream.remove(sensorRecord.getLicencePlate());

		if (time <= 0) {
			return null;
		}

		int speed = (int) Math.round((0.5 / time) * 3600);
		return new SpeedRecord(sensorRecord.getLicencePlate(), speed, time);
	}

	private int calculateTime(SensorRecord record1, SensorRecord record2) {
		int time1 = (record1.getMinute() * 60) + record1.getSecond();
		int time2 = (record2.getMinute() * 60) + record2.getSecond();
		int difference = Math.abs(time2 - time1);

		if (difference > 1800) {
			difference = 3600 - difference;
		}

		return difference;
	}

}
