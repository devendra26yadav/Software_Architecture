package employees;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class EmployeeController {
    @GetMapping("/name")
    public String getName() {
        return "Frank Brown";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/salary")
    public String getSalary() {
        return "95.000";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/phone")
    public String getPhone() {
        return "645322899";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sensitive")
    public String getPrivateData(@AuthenticationPrincipal Jwt jwt) {
        String token = jwt.getTokenValue();
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
        // Call the downstream application with the same token
        String response = webClient.get()
                .uri("/private")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
