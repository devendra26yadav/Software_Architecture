package esb;

public class OrderTypeRouter {
	private static final String INTERNATIONAL = "international";

	public String route(Order order) {
		if (order.getOrderType() != null && INTERNATIONAL.equalsIgnoreCase(order.getOrderType().trim())) {
			return "internationalShippingChannel";
		}
		return "domesticShippingRouterChannel";
	}
}
