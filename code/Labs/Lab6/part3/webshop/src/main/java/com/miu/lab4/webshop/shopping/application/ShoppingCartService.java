package com.miu.lab4.webshop.shopping.application;

import com.miu.lab4.webshop.shopping.domain.CartItem;
import com.miu.lab4.webshop.shopping.domain.ShoppingCart;
import com.miu.lab4.webshop.shopping.infrastructure.ProductClient;
import com.miu.lab4.webshop.shopping.infrastructure.ProductSnapshot;
import com.miu.lab4.webshop.shopping.infrastructure.ShoppingCartRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

  private final ShoppingCartRepository shoppingCartRepository;
  private final ProductClient productClient;

  public ShoppingCartService(
      ShoppingCartRepository shoppingCartRepository,
      ProductClient productClient) {
    this.shoppingCartRepository = shoppingCartRepository;
    this.productClient = productClient;
  }

  public ShoppingCart addToShoppingCart(String customerNumber, String productNumber, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be at least 1");
    }

    ProductSnapshot product = productClient.getProduct(productNumber);
    ShoppingCart cart = shoppingCartRepository.findById(customerNumber)
        .orElse(new ShoppingCart(customerNumber));

    cart.addOrIncreaseItem(product, quantity);
    return shoppingCartRepository.save(cart);
  }

  public ShoppingCart getShoppingCart(String customerNumber) {
    ShoppingCart cart = shoppingCartRepository.findById(customerNumber)
        .orElseGet(() -> shoppingCartRepository.save(new ShoppingCart(customerNumber)));

    boolean changed = false;
    for (CartItem item : cart.getItems()) {
      try {
        ProductSnapshot latest = productClient.getProduct(item.getProductNumber());
        changed = cart.refreshItem(latest) || changed;
      } catch (NoSuchElementException ignored) {
        // Keep existing cart data even if product is no longer available.
      }
    }

    if (changed) {
      return shoppingCartRepository.save(cart);
    }
    return cart;
  }
}
