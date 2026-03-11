package mydomain.com.productservice;

import mydomain.com.productservice.dto.ProductResponse;
import mydomain.com.productservice.service.StockClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final StockClient stockClient;

    public ProductController(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @GetMapping("/api/products/{productNumber}")
    public ProductResponse getProduct(@PathVariable int productNumber) {
        int stock = stockClient.getStock(productNumber);
        return new ProductResponse(productNumber, "Product-" + productNumber, stock);
    }
}
