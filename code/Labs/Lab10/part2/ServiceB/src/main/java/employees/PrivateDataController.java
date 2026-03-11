package employees;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateDataController {
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
    @GetMapping("/employee-contact-data")
    public String getEmployeeContactData() {
        return "Employee contact data: 515-555-0101";
    }
}
