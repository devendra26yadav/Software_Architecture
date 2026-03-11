package com.miu.lab6.clientapp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LabClientRunner {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${client.product-base-url}")
  private String productBaseUrl;

  @Value("${client.shopping-base-url}")
  private String shoppingBaseUrl;

  @Value("${client.customer-number}")
  private String customerNumber;

  public LabClientRunner(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void runClientFlow() {
    String productNumber = generateProductNumber();

    print("1) Added product", addProduct(productNumber));
    print("2) Fetched product by productNumber", getProduct(productNumber));
    print("3) Added product to shopping cart", addToCart(productNumber, 3));
    print("4) Retrieved shopping cart", getCart());

    updatePrice(productNumber, new BigDecimal("79.99"));
    print("5) Changed product price", getProduct(productNumber));
    print("6) Retrieved shopping cart after price change", getCart());

    System.exit(0);
  }

  private String generateProductNumber() {
    return "P-" + System.currentTimeMillis();
  }

  private Object addProduct(String productNumber) {
    AddProductRequest request = new AddProductRequest(
        productNumber,
        "Architecture in Practice",
        new BigDecimal("49.99"),
        "Software Architecture Lab Product");
    return restTemplate.postForObject(productBaseUrl, request, Object.class);
  }

  private Object getProduct(String productNumber) {
    return restTemplate.getForObject(
        productBaseUrl + "/{productNumber}",
        Object.class,
        productNumber);
  }

  private Object addToCart(String productNumber, int quantity) {
    AddToCartRequest request = new AddToCartRequest(productNumber, quantity);
    return restTemplate.postForObject(
        shoppingBaseUrl + "/{customerNumber}/items",
        request,
        Object.class,
        customerNumber);
  }

  private Object getCart() {
    return restTemplate.getForObject(
        shoppingBaseUrl + "/{customerNumber}",
        Object.class,
        customerNumber);
  }

  private void updatePrice(String productNumber, BigDecimal newPrice) {
    UpdatePriceRequest request = new UpdatePriceRequest(newPrice);
    restTemplate.put(productBaseUrl + "/{productNumber}/price", request, productNumber);
  }

  private void print(String title, Object payload) {
    try {
      System.out.println("\n=== " + title + " ===");
      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
    } catch (JsonProcessingException ex) {
      System.out.println("\n=== " + title + " ===");
      System.out.println(payload);
    }
  }

  private record AddProductRequest(
      String productNumber,
      String name,
      BigDecimal price,
      String description) {
  }

  private record AddToCartRequest(
      String productNumber,
      int quantity) {
  }

  private record UpdatePriceRequest(BigDecimal price) {
  }
}
