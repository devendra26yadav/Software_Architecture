package mydomain.com.stockservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @GetMapping("/stock/{productNumber}")
    public int  getStock(@PathVariable String productNumber){
        return switch(productNumber){
            case "P100" -> 23;
            case "P200" -> 7;
            case "P300" -> 8;
            default -> 0;
        };
    }
}
