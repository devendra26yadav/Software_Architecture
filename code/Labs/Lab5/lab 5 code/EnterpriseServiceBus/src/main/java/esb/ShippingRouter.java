package esb;

public class ShippingRouter {
	private static final double NEXT_DAY_THRESHOLD = 175.0;

	public String route(Order order) {
		if (order.getAmount() > NEXT_DAY_THRESHOLD) {
			return "nextDayShippingChannel";
		}
		return "normalShippingChannel";
	}
}
