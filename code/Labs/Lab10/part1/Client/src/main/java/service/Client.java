package service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@SpringBootApplication
public class Client implements CommandLineRunner {

	@Value("${security.demo.token-url}")
	private String tokenUrl;

	@Value("${security.demo.client-id}")
	private String clientId;

	@Value("${security.demo.client-secret}")
	private String clientSecret;

	public static void main(String[] args) {
		SpringApplication.run(Client.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		callRemoteService("http://localhost:8081/name", "nobody", "nobody");
		callRemoteService("http://localhost:8081/phone", "john", "john");
		callRemoteService("http://localhost:8081/phone", "frank", "frank");
		callRemoteService("http://localhost:8081/salary", "john", "john");
		callRemoteService("http://localhost:8081/salary", "frank", "frank");
		callRemoteService("http://localhost:8081/sensitive", "frank", "frank");
	}

	public void callRemoteService(String url, String username, String password){
		String accessToken = getAccessToken(username, password);
			// Call resource server
			String response = WebClient.create(url)
					.get()
					.headers(h ->{
						if (accessToken != null)
							h.setBearerAuth(accessToken);
					})
					.retrieve()
					.onStatus(status -> status.value() == 401, clientResponse -> {
						System.out.println("Error: Unauthorized - token missing or expired for call "+url+" with user "+username);
						return Mono.empty(); // Do NOT throw
					})
					.onStatus(status -> status.value() == 403, clientResponse -> {
						System.out.println("Forbidden: insufficient roles "+url+" with user "+username);
						return Mono.empty(); // Do NOT throw
					})
					.bodyToMono(String.class)
					.block();

		System.out.println("Call to " +url+ " for user "+username+" gave the response "+ response);
	}

	public String getAccessToken(String username, String password){
		WebClient webClient = WebClient.builder()
				.baseUrl(tokenUrl)
				.build();
		// Get access token
		Map<String, Object> tokenResponse = webClient.post()
				.header("Content-Type", "application/x-www-form-urlencoded")
				.body(BodyInserters.fromFormData("grant_type", "password")
						.with("username", username)
						.with("password", password)
						.with("scope", "openid roles")
						.with("client_id", clientId)
						.with("client_secret", clientSecret))
				.retrieve()
				.onStatus(status -> status.value() == 401, clientResponse -> {
					System.out.println("No token for user "+username);
					return Mono.empty(); // Do NOT throw
				})
				.bodyToMono(Map.class)
				.block();

		if (tokenResponse == null) {
			return null;
		}
		String accessToken = (String) tokenResponse.get("access_token");
		return accessToken;
	}
}
