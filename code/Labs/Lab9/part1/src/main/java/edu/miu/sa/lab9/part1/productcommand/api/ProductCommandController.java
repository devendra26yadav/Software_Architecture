package edu.miu.sa.lab9.part1.productcommand.api;

import edu.miu.sa.lab9.part1.productcommand.application.ProductCommandService;
import edu.miu.sa.lab9.part1.productcommand.domain.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    public ProductCommandController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductRequest request) {
        return productCommandService.addProduct(request.productNumber(), request.name(), request.price());
    }

    @PutMapping("/{productNumber}")
    public Product updateProduct(@PathVariable String productNumber, @Valid @RequestBody ProductUpdateRequest request) {
        return productCommandService.updateProduct(productNumber, request.name(), request.price());
    }

    @DeleteMapping("/{productNumber}")
    public void deleteProduct(@PathVariable String productNumber) {
        productCommandService.deleteProduct(productNumber);
    }

    public record ProductRequest(
            @NotBlank String productNumber,
            @NotBlank String name,
            @NotNull @DecimalMin("0.0") BigDecimal price
    ) {
    }

    public record ProductUpdateRequest(
            @NotBlank String name,
            @NotNull @DecimalMin("0.0") BigDecimal price
    ) {
    }
}

