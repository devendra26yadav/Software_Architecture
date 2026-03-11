package esb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

	@PostMapping("/monitor/events")
	public ResponseEntity<?> receiveEvent(@RequestBody MonitoringEvent event) {
		System.out.println(formatEvent(event));
		return new ResponseEntity<MonitoringEvent>(event, HttpStatus.OK);
	}

	private String formatEvent(MonitoringEvent event) {
		return "[" + nullSafe(event.getTimestamp()) + "] "
				+ nullSafe(event.getSource()) + " | "
				+ nullSafe(event.getStep()) + " | "
				+ "order=" + nullSafe(event.getOrderNumber()) + " | "
				+ "amount=" + (event.getAmount() == null ? "n/a" : event.getAmount()) + " | "
				+ "type=" + nullSafe(event.getOrderType()) + " | "
				+ "payment=" + nullSafe(event.getPaymentMethod()) + " | "
				+ nullSafe(event.getDetails());
	}

	private String nullSafe(String value) {
		return value == null ? "n/a" : value;
	}
}
