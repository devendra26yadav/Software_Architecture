package com.miu.lab4.webshop.product.domain;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

  @Id
  private String productNumber;
  private String name;
  private BigDecimal price;
  private String description;

  public Product() {
  }

  public Product(String productNumber, String name, BigDecimal price, String description) {
    this.productNumber = productNumber;
    this.name = name;
    this.price = price;
    this.description = description;
  }

  public String getProductNumber() {
    return productNumber;
  }

  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
