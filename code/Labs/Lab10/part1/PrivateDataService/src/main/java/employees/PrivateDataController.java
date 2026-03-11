package employees;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateDataController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private")
    public String getPrivateData() {
        return "This is very private data";
    }
}
