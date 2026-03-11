package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class VisaPaymentActivator {
	private static final String VISA_PAYMENT_URL = "http://localhost:8083/payments/visa";

	@Autowired
	RestTemplate restTemplate;

	public void pay(Order order) {
		System.out.println("PaymentService visa payment for order: " + order);
		restTemplate.postForLocation(VISA_PAYMENT_URL, order);
	}
}
