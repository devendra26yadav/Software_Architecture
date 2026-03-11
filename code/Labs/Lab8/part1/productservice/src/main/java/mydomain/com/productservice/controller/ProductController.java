package mydomain.com.productservice.controller;

import mydomain.com.productservice.service.StockServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final StockServiceClient stockServiceClient;
    public ProductController(StockServiceClient stockServiceClient) {
        this.stockServiceClient = stockServiceClient;
    }

    @GetMapping("/product/{productNUmber}")
    String getProduct(@PathVariable("productNUmber") String productNUmber) {
        int stocks = stockServiceClient.getStock(productNUmber);
        return "Product "+productNUmber + " has "+stocks +" stocks";
    }
}
