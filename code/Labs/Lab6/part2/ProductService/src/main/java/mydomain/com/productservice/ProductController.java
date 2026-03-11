package mydomain.com.productservice;

import mydomain.com.productservice.dto.ProductResponseDto;
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

    @GetMapping("/product/{productNumber}")
    public ProductResponseDto getProduct(@PathVariable("productNumber") String productNumber){
        String name = switch (productNumber){
            case "P100"->"iPhone";
            case "P200"->"Mouse";
            default->"Unknown Product";
        };
        int quantity = stockClient.getStock(productNumber);
        return new ProductResponseDto(productNumber,name,quantity);
    }
}
