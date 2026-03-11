package com.miu.lab4.webshop.shopping.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AddToCartRequest {

  @NotBlank
  private String productNumber;

  @Min(1)
  private int quantity;

  public String getProductNumber() {
    return productNumber;
  }

  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
