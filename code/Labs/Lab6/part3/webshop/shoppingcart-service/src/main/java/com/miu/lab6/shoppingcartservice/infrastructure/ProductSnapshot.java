package com.miu.lab6.shoppingcartservice.infrastructure;

import java.math.BigDecimal;

public class ProductSnapshot {

  private String productNumber;
  private String name;
  private BigDecimal price;
  private String description;

  public ProductSnapshot() {
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
