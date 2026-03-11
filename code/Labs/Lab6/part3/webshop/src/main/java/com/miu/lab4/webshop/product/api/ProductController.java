package com.miu.lab4.webshop.product.api;

import com.miu.lab4.webshop.product.application.ProductService;
import com.miu.lab4.webshop.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<Product> addProduct(@Valid @RequestBody AddProductRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
  }

  @GetMapping("/{productNumber}")
  public Product getProduct(@PathVariable String productNumber) {
    return productService.getProduct(productNumber);
  }

  @PatchMapping("/{productNumber}/price")
  public Product changePrice(
      @PathVariable String productNumber,
      @Valid @RequestBody UpdatePriceRequest request) {
    return productService.changePrice(productNumber, request.getPrice());
  }

  @PutMapping("/{productNumber}/price")
  public Product replacePrice(
      @PathVariable String productNumber,
      @Valid @RequestBody UpdatePriceRequest request) {
    return productService.changePrice(productNumber, request.getPrice());
  }
}
