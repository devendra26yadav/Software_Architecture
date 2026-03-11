package esb;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MonitoringGateway {
	private static final String MONITORING_ENDPOINT = "http://localhost:8084/monitor/events";
	private static final String SOURCE = "EnterpriseServiceBus";

	private final RestTemplate restTemplate = new RestTemplate();

	public void track(String step, Order order, String details) {
		Map<String, Object> event = new LinkedHashMap<String, Object>();
		event.put("source", SOURCE);
		event.put("step", step);
		event.put("timestamp", OffsetDateTime.now().toString());
		event.put("details", details);

		if (order != null) {
			event.put("orderNumber", order.getOrderNumber());
			event.put("amount", order.getAmount());
			event.put("orderType", order.getOrderType());
			event.put("paymentMethod", order.getPaymentMethod());
		}

		try {
			restTemplate.postForLocation(MONITORING_ENDPOINT, event);
		} catch (Exception ignored) {
			System.out.println("MonitoringService is unavailable. Event not sent.");
		}
	}
}
