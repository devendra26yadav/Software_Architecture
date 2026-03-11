package esb;

public class PaymentRouter {
	private static final String MASTERCARD = "mastercard";
	private static final String VISA = "visa";
	private static final String PAYPAL = "paypal";

	public String route(Order order) {
		String paymentMethod = order.getPaymentMethod();
		if (paymentMethod == null) {
			throw new IllegalArgumentException("paymentMethod is required");
		}

		String normalized = paymentMethod.trim().toLowerCase();
		if (MASTERCARD.equals(normalized)) {
			return "mastercardPaymentChannel";
		}
		if (VISA.equals(normalized)) {
			return "visaPaymentChannel";
		}
		if (PAYPAL.equals(normalized)) {
			return "paypalPaymentChannel";
		}
		throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
	}
}
