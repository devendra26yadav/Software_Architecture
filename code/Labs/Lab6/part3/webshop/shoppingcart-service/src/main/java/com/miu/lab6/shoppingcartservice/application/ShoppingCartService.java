package com.miu.lab6.shoppingcartservice.application;

import com.miu.lab6.shoppingcartservice.domain.CartItem;
import com.miu.lab6.shoppingcartservice.domain.ShoppingCart;
import com.miu.lab6.shoppingcartservice.infrastructure.ProductClient;
import com.miu.lab6.shoppingcartservice.infrastructure.ProductSnapshot;
import com.miu.lab6.shoppingcartservice.infrastructure.ShoppingCartRepository;
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
    validateQuantity(quantity);

    ProductSnapshot product = productClient.getProduct(productNumber);
    ShoppingCart cart = findOrCreateCart(customerNumber);
    cart.addOrIncreaseItem(product, quantity);

    return shoppingCartRepository.save(cart);
  }

  public ShoppingCart getShoppingCart(String customerNumber) {
    ShoppingCart cart = getOrCreatePersistedCart(customerNumber);
    boolean cartUpdated = refreshItemsWithLatestProductData(cart);

    if (cartUpdated) {
      return shoppingCartRepository.save(cart);
    }
    return cart;
  }

  private void validateQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be at least 1");
    }
  }

  private ShoppingCart findOrCreateCart(String customerNumber) {
    return shoppingCartRepository.findById(customerNumber)
        .orElse(new ShoppingCart(customerNumber));
  }

  private ShoppingCart getOrCreatePersistedCart(String customerNumber) {
    return shoppingCartRepository.findById(customerNumber)
        .orElseGet(() -> shoppingCartRepository.save(new ShoppingCart(customerNumber)));
  }

  private boolean refreshItemsWithLatestProductData(ShoppingCart cart) {
    boolean cartUpdated = false;
    for (CartItem item : cart.getItems()) {
      try {
        ProductSnapshot latest = productClient.getProduct(item.getProductNumber());
        cartUpdated = cart.refreshItem(latest) || cartUpdated;
      } catch (NoSuchElementException ignored) {
        // Keep existing cart data if the product is currently unavailable.
      }
    }
    return cartUpdated;
  }
}
