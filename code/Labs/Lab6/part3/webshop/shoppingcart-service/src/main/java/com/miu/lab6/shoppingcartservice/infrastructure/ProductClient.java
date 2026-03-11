package com.miu.lab6.shoppingcartservice.infrastructure;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

  private final RestTemplate restTemplate;
  private final String productBaseUrl;

  public ProductClient(
      RestTemplate restTemplate,
      @Value("${clients.product-service.base-url}") String productBaseUrl) {
    this.restTemplate = restTemplate;
    this.productBaseUrl = productBaseUrl;
  }

  public ProductSnapshot getProduct(String productNumber) {
    try {
      ProductSnapshot snapshot = restTemplate.getForObject(
          productBaseUrl + "/{productNumber}",
          ProductSnapshot.class,
          productNumber);
      return requireSnapshot(snapshot, productNumber);
    } catch (HttpClientErrorException.NotFound ex) {
      throw productNotFound(productNumber);
    } catch (RestClientException ex) {
      throw new IllegalStateException(
          "Unable to reach product-service via registry. Check Consul and product-service status.",
          ex);
    }
  }

  private ProductSnapshot requireSnapshot(ProductSnapshot snapshot, String productNumber) {
    if (snapshot == null) {
      throw productNotFound(productNumber);
    }
    return snapshot;
  }

  private NoSuchElementException productNotFound(String productNumber) {
    return new NoSuchElementException("Product not found: " + productNumber);
  }
}
