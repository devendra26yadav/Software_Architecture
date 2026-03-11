package com.miu.lab4.webshop.product.application;

import com.miu.lab4.webshop.product.api.AddProductRequest;
import com.miu.lab4.webshop.product.domain.Product;
import com.miu.lab4.webshop.product.infrastructure.ProductRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product addProduct(AddProductRequest request) {
    if (productRepository.existsById(request.getProductNumber())) {
      throw new IllegalArgumentException("Product already exists: " + request.getProductNumber());
    }

    Product product = new Product(
        request.getProductNumber().trim(),
        request.getName().trim(),
        request.getPrice(),
        request.getDescription().trim());

    return productRepository.save(product);
  }

  public Product getProduct(String productNumber) {
    return productRepository.findById(productNumber)
        .orElseThrow(() -> new NoSuchElementException("Product not found: " + productNumber));
  }

  public Product changePrice(String productNumber, java.math.BigDecimal newPrice) {
    Product product = getProduct(productNumber);
    product.setPrice(newPrice);
    return productRepository.save(product);
  }
}
