package com.miu.lab4.webshop.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miu.lab4.webshop.product.api.UpdatePriceRequest;
import com.miu.lab4.webshop.shopping.api.AddToCartRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnProperty(name = "lab4.client.enabled", havingValue = "true")
public class Lab4RestClientRunner {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${lab4.client.base-url:http://localhost:8080/api}")
  private String baseUrl;

  @Value("${lab4.client.customer-number:CUST-100}")
  private String customerNumber;

  public Lab4RestClientRunner(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void runClientFlow() {
    String productNumber = "P-" + System.currentTimeMillis();

    Map<String, Object> addProductRequest = new LinkedHashMap<>();
    addProductRequest.put("productNumber", productNumber);
    addProductRequest.put("name", "Architecture in Practice");
    addProductRequest.put("price", new BigDecimal("49.99"));
    addProductRequest.put("description", "Software Architecture Lab Product");

    Object step1 = restTemplate.postForObject(baseUrl + "/products", addProductRequest, Object.class);
    print("1) Added product", step1);

    Object step2 = restTemplate.getForObject(baseUrl + "/products/{productNumber}", Object.class, productNumber);
    print("2) Fetched product by productNumber", step2);

    AddToCartRequest addToCartRequest = new AddToCartRequest();
    addToCartRequest.setProductNumber(productNumber);
    addToCartRequest.setQuantity(3);
    Object step3 = restTemplate.postForObject(
        baseUrl + "/shopping-carts/{customerNumber}/items",
        addToCartRequest,
        Object.class,
        customerNumber);
    print("3) Added product to shopping cart", step3);

    Object step4 = restTemplate.getForObject(
        baseUrl + "/shopping-carts/{customerNumber}",
        Object.class,
        customerNumber);
    print("4) Retrieved shopping cart", step4);

    UpdatePriceRequest updatePriceRequest = new UpdatePriceRequest();
    updatePriceRequest.setPrice(new BigDecimal("79.99"));
    restTemplate.put(baseUrl + "/products/{productNumber}/price", updatePriceRequest, productNumber);
    Object step5 = restTemplate.getForObject(baseUrl + "/products/{productNumber}", Object.class, productNumber);
    print("5) Changed product price", step5);

    Object step6 = restTemplate.getForObject(
        baseUrl + "/shopping-carts/{customerNumber}",
        Object.class,
        customerNumber);
    print("6) Retrieved shopping cart after price change", step6);
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
}
