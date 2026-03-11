package employees;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateDataController {
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/salary-data")
    public String getSalaryData() {
        return "Salary data: USD 95,000";
    }
}
