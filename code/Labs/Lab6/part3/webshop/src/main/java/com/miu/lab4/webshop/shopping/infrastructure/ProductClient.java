package com.miu.lab4.webshop.shopping.infrastructure;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

  private final RestTemplate restTemplate;
  private final String productBaseUrl;

  public ProductClient(
      RestTemplate restTemplate,
      @Value("${components.product.base-url:http://localhost:8080/api/products}") String productBaseUrl) {
    this.restTemplate = restTemplate;
    this.productBaseUrl = productBaseUrl;
  }

  public ProductSnapshot getProduct(String productNumber) {
    try {
      ProductSnapshot snapshot = restTemplate.getForObject(
          productBaseUrl + "/{productNumber}",
          ProductSnapshot.class,
          productNumber);

      if (snapshot == null) {
        throw new NoSuchElementException("Product not found: " + productNumber);
      }
      return snapshot;
    } catch (HttpClientErrorException.NotFound ex) {
      throw new NoSuchElementException("Product not found: " + productNumber);
    }
  }
}
