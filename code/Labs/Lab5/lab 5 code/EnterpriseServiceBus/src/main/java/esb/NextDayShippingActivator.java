package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class NextDayShippingActivator {
	private static final String NEXT_DAY_SHIPPING_URL = "http://localhost:8082/orders/next-day";

	@Autowired
	RestTemplate restTemplate;

	public Order ship(Order order) {
		System.out.println("ShippingService next-day shipping for order: " + order);
		restTemplate.postForLocation(NEXT_DAY_SHIPPING_URL, order);
		return order;
	}
}
