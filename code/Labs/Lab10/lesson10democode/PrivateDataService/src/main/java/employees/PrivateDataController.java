package employees;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class PrivateDataController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private")
    public String getPrivateData() {
        return "This is very private data";
    }
}
