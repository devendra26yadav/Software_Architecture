package mydomain.com.productservice.controller;

import mydomain.com.productservice.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{productNumber}")
    String getProduct(@PathVariable("productNumber") String productNumber) {
        return productService.getProduct(productNumber);
    }
}
