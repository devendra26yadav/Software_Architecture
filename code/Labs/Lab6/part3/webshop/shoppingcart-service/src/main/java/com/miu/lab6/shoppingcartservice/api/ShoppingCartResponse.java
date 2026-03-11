package com.miu.lab6.shoppingcartservice.api;

import com.miu.lab6.shoppingcartservice.domain.CartItem;
import com.miu.lab6.shoppingcartservice.domain.ShoppingCart;
import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartResponse {

  private String customerNumber;
  private List<ShoppingCartItemResponse> items;
  private BigDecimal total;

  public static ShoppingCartResponse from(ShoppingCart cart) {
    ShoppingCartResponse response = new ShoppingCartResponse();
    response.setCustomerNumber(cart.getCustomerNumber());
    response.setItems(cart.getItems().stream().map(ShoppingCartResponse::mapItem).toList());
    response.setTotal(cart.total());
    return response;
  }

  private static ShoppingCartItemResponse mapItem(CartItem item) {
    ShoppingCartItemResponse response = new ShoppingCartItemResponse();
    response.setProductNumber(item.getProductNumber());
    response.setProductName(item.getProductName());
    response.setDescription(item.getDescription());
    response.setPrice(item.getPrice());
    response.setQuantity(item.getQuantity());
    response.setLineTotal(item.lineTotal());
    return response;
  }

  public String getCustomerNumber() {
    return customerNumber;
  }

  public void setCustomerNumber(String customerNumber) {
    this.customerNumber = customerNumber;
  }

  public List<ShoppingCartItemResponse> getItems() {
    return items;
  }

  public void setItems(List<ShoppingCartItemResponse> items) {
    this.items = items;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
