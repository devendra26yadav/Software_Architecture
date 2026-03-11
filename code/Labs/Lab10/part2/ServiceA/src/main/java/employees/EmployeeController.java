package employees;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class EmployeeController {

    @Value("${services.contact.base-url}")
    private String contactServiceBaseUrl;

    @Value("${services.salary.base-url}")
    private String salaryServiceBaseUrl;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE', 'MANAGER')")
    @GetMapping("/productdata")
    public String getProductData() {
        return "Product data: laptop, phone, monitor";
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
    @GetMapping("/employee-contact-data")
    public String getEmployeeContactData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return callDownstreamService(contactServiceBaseUrl, "/employee-contact-data", authorizationHeader);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/salary-data")
    public String getSalaryData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return callDownstreamService(salaryServiceBaseUrl, "/salary-data", authorizationHeader);
    }

    private String callDownstreamService(String baseUrl, String path, String authorizationHeader) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(path)
                .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .retrieve()
                .onStatus(status -> status.value() == 401, response ->
                        Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Downstream service rejected credentials")))
                .onStatus(status -> status.value() == 403, response ->
                        Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Downstream service denied access")))
                .bodyToMono(String.class)
                .block();
    }
}
