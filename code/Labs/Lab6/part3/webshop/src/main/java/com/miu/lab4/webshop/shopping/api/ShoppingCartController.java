package com.miu.lab4.webshop.shopping.api;

import com.miu.lab4.webshop.shopping.application.ShoppingCartService;
import com.miu.lab4.webshop.shopping.domain.ShoppingCart;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {

  private final ShoppingCartService shoppingCartService;

  public ShoppingCartController(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }

  @PostMapping("/{customerNumber}/items")
  public ResponseEntity<ShoppingCartResponse> addToShoppingCart(
      @PathVariable String customerNumber,
      @Valid @RequestBody AddToCartRequest request) {

    ShoppingCart cart = shoppingCartService.addToShoppingCart(
        customerNumber,
        request.getProductNumber(),
        request.getQuantity());

    return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingCartResponse.from(cart));
  }

  @GetMapping("/{customerNumber}")
  public ShoppingCartResponse getShoppingCart(@PathVariable String customerNumber) {
    return ShoppingCartResponse.from(shoppingCartService.getShoppingCart(customerNumber));
  }
}
