package edu.miu.sa.lab9.part2.productquery.api;

import edu.miu.sa.lab9.part2.productquery.application.ProductQueryService;
import edu.miu.sa.lab9.part2.productquery.domain.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/query/products")
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    public ProductQueryController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping
    public List<ProductView> findAll() {
        return productQueryService.findAll();
    }

    @GetMapping("/{productNumber}")
    public ProductView findByProductNumber(@PathVariable String productNumber) {
        return productQueryService.findByProductNumber(productNumber);
    }
}

