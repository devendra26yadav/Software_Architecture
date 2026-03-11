package esb;

public class Order {
	private String orderNumber;
	private double amount;
	private String orderType;
	private String paymentMethod;

	public Order() {
	}

	public Order(String orderNumber, double amount) {
		super();
		this.orderNumber = orderNumber;
		this.amount = amount;
		this.orderType = "domestic";
		this.paymentMethod = "mastercard";
	}

	public Order(String orderNumber, double amount, String orderType) {
		super();
		this.orderNumber = orderNumber;
		this.amount = amount;
		this.orderType = orderType;
		this.paymentMethod = "mastercard";
	}

	public Order(String orderNumber, double amount, String orderType, String paymentMethod) {
		super();
		this.orderNumber = orderNumber;
		this.amount = amount;
		this.orderType = orderType;
		this.paymentMethod = paymentMethod;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderNumber='" + orderNumber + '\'' +
				", amount=" + amount +
				", orderType='" + orderType + '\'' +
				", paymentMethod='" + paymentMethod + '\'' +
				'}';
	}
}
