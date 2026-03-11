package mydomain.com.stockservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/api/stock/{productNumber}")
    public int getStock(@PathVariable int productNumber) {
        return 10 + (productNumber % 5); // stock service 1
    }
}
