package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class InternationalShippingActivator {
	private static final String INTERNATIONAL_SHIPPING_URL = "http://localhost:8082/orders/international";

	@Autowired
	RestTemplate restTemplate;

	public Order ship(Order order) {
		System.out.println("ShippingService international shipping for order: " + order);
		restTemplate.postForLocation(INTERNATIONAL_SHIPPING_URL, order);
		return order;
	}
}
