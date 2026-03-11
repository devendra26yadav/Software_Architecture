package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class NormalShippingActivator {
	private static final String NORMAL_SHIPPING_URL = "http://localhost:8082/orders/normal";

	@Autowired
	RestTemplate restTemplate;

	public Order ship(Order order) {
		System.out.println("ShippingService normal shipping for order: " + order);
		restTemplate.postForLocation(NORMAL_SHIPPING_URL, order);
		return order;
	}
}
