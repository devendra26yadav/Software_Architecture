package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Client implements CommandLineRunner {

	@Value("${lab.service-a-base-url}")
	private String serviceABaseUrl;

	public static void main(String[] args) {
		SpringApplication.run(Client.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		callRemoteService("/productdata", "customer", "customer");
		callRemoteService("/productdata", "employee", "employee");
		callRemoteService("/productdata", "manager", "manager");

		callRemoteService("/employee-contact-data", "customer", "customer");
		callRemoteService("/employee-contact-data", "employee", "employee");
		callRemoteService("/employee-contact-data", "manager", "manager");

		callRemoteService("/salary-data", "customer", "customer");
		callRemoteService("/salary-data", "employee", "employee");
		callRemoteService("/salary-data", "manager", "manager");
	}

	public void callRemoteService(String path, String username, String password){
		String url = serviceABaseUrl + path;
		String response = WebClient.create(url)
				.get()
				.headers(headers -> headers.setBasicAuth(username, password))
				.retrieve()
				.onStatus(status -> status.value() == 401, clientResponse -> {
					System.out.println("Unauthorized " + path + " with user " + username);
					return Mono.empty();
				})
				.onStatus(status -> status.value() == 403, clientResponse -> {
					System.out.println("Forbidden " + path + " with user " + username);
					return Mono.empty();
				})
				.bodyToMono(String.class)
				.block();

		System.out.println("Call to " + url + " for user " + username + " gave the response " + response);
	}
}
