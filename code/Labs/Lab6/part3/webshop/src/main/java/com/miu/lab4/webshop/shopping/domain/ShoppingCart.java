package com.miu.lab4.webshop.shopping.domain;

import com.miu.lab4.webshop.shopping.infrastructure.ProductSnapshot;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shopping_carts")
public class ShoppingCart {

  @Id
  private String customerNumber;
  private List<CartItem> items = new ArrayList<>();

  public ShoppingCart() {
  }

  public ShoppingCart(String customerNumber) {
    this.customerNumber = customerNumber;
  }

  public String getCustomerNumber() {
    return customerNumber;
  }

  public void setCustomerNumber(String customerNumber) {
    this.customerNumber = customerNumber;
  }

  public List<CartItem> getItems() {
    return items;
  }

  public void setItems(List<CartItem> items) {
    this.items = items;
  }

  public void addOrIncreaseItem(ProductSnapshot product, int quantity) {
    Optional<CartItem> existingItem = items.stream()
        .filter(item -> item.getProductNumber().equals(product.getProductNumber()))
        .findFirst();

    if (existingItem.isPresent()) {
      CartItem item = existingItem.get();
      item.increaseQuantity(quantity);
      item.refreshDetails(product.getName(), product.getDescription(), product.getPrice());
      return;
    }

    CartItem newItem = new CartItem(
        product.getProductNumber(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        quantity);
    items.add(newItem);
  }

  public boolean refreshItem(ProductSnapshot product) {
    for (CartItem item : items) {
      if (item.getProductNumber().equals(product.getProductNumber())) {
        boolean changed = !item.getPrice().equals(product.getPrice())
            || !item.getProductName().equals(product.getName())
            || !item.getDescription().equals(product.getDescription());
        if (changed) {
          item.refreshDetails(product.getName(), product.getDescription(), product.getPrice());
        }
        return changed;
      }
    }
    return false;
  }

  public BigDecimal total() {
    return items.stream()
        .map(CartItem::lineTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
