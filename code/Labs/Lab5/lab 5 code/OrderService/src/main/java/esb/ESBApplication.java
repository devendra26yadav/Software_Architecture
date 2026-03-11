package esb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class ESBApplication implements CommandLineRunner {
	private static final String ESB_ORDER_ENDPOINT = "http://localhost:8080/orders";

	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private MonitoringGateway monitoringGateway;

	public static void main(String[] args) {
		SpringApplication.run(ESBApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		submitOrder(new Order("334", 120.0, "domestic", "mastercard"));
		submitOrder(new Order("355", 185.0, "domestic", "visa"));
		submitOrder(new Order("377", 95.0, "international", "paypal"));
	}

	private void submitOrder(Order order) {
		monitoringGateway.track("ORDER_SUBMITTED", order, "Submitting order to ESB /orders.");
		restTemplate.postForLocation(ESB_ORDER_ENDPOINT, order);
	}
}
