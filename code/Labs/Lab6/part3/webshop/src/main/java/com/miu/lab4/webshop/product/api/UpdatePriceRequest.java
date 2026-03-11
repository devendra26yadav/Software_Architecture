package com.miu.lab4.webshop.product.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UpdatePriceRequest {

  @NotNull
  @DecimalMin(value = "0.01", inclusive = true)
  private BigDecimal price;

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
