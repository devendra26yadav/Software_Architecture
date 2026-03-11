package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class MastercardPaymentActivator {
	private static final String MASTERCARD_PAYMENT_URL = "http://localhost:8083/payments/mastercard";

	@Autowired
	RestTemplate restTemplate;

	public void pay(Order order) {
		System.out.println("PaymentService mastercard payment for order: " + order);
		restTemplate.postForLocation(MASTERCARD_PAYMENT_URL, order);
	}
}
