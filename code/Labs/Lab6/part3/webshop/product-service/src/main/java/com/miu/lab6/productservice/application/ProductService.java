package com.miu.lab6.productservice.application;

import com.miu.lab6.productservice.api.AddProductRequest;
import com.miu.lab6.productservice.domain.Product;
import com.miu.lab6.productservice.infrastructure.ProductRepository;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product addProduct(AddProductRequest request) {
    String productNumber = request.getProductNumber().trim();
    if (productRepository.existsById(productNumber)) {
      throw new IllegalArgumentException("Product already exists: " + productNumber);
    }

    Product product = new Product(
        productNumber,
        request.getName().trim(),
        request.getPrice(),
        request.getDescription().trim());

    return productRepository.save(product);
  }

  public Product getProduct(String productNumber) {
    return productRepository.findById(productNumber)
        .orElseThrow(() -> new NoSuchElementException("Product not found: " + productNumber));
  }

  public Product changePrice(String productNumber, BigDecimal newPrice) {
    Product product = getProduct(productNumber);
    product.setPrice(newPrice);
    return productRepository.save(product);
  }
}
