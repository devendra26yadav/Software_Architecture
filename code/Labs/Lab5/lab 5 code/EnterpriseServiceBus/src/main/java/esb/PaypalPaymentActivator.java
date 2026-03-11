package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PaypalPaymentActivator {
	private static final String PAYPAL_PAYMENT_URL = "http://localhost:8083/payments/paypal";

	@Autowired
	RestTemplate restTemplate;

	public void pay(Order order) {
		System.out.println("PaymentService paypal payment for order: " + order);
		restTemplate.postForLocation(PAYPAL_PAYMENT_URL, order);
	}
}
