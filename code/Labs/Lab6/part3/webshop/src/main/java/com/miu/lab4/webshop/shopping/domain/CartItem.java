package com.miu.lab4.webshop.shopping.domain;

import java.math.BigDecimal;

public class CartItem {

  private String productNumber;
  private String productName;
  private String description;
  private BigDecimal price;
  private int quantity;

  public CartItem() {
  }

  public CartItem(String productNumber, String productName, String description, BigDecimal price, int quantity) {
    this.productNumber = productNumber;
    this.productName = productName;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
  }

  public String getProductNumber() {
    return productNumber;
  }

  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void increaseQuantity(int additionalQuantity) {
    this.quantity += additionalQuantity;
  }

  public void refreshDetails(String name, String newDescription, BigDecimal newPrice) {
    this.productName = name;
    this.description = newDescription;
    this.price = newPrice;
  }

  public BigDecimal lineTotal() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }
}
