package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

	@Autowired
	private MonitoringGateway monitoringGateway;

	@PostMapping("/payments/mastercard")
	public ResponseEntity<?> payWithMastercard(@RequestBody Order order) {
		System.out.println("PaymentService processed MASTERCARD payment: " + order);
		monitoringGateway.track("PAYMENT_MASTERCARD_PROCESSED", order, "Payment endpoint /payments/mastercard called.");
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	@PostMapping("/payments/visa")
	public ResponseEntity<?> payWithVisa(@RequestBody Order order) {
		System.out.println("PaymentService processed VISA payment: " + order);
		monitoringGateway.track("PAYMENT_VISA_PROCESSED", order, "Payment endpoint /payments/visa called.");
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	@PostMapping("/payments/paypal")
	public ResponseEntity<?> payWithPaypal(@RequestBody Order order) {
		System.out.println("PaymentService processed PAYPAL payment: " + order);
		monitoringGateway.track("PAYMENT_PAYPAL_PROCESSED", order, "Payment endpoint /payments/paypal called.");
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
}
