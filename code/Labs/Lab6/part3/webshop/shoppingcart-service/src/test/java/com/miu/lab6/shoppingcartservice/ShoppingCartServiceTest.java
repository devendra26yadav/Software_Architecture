package com.miu.lab6.shoppingcartservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miu.lab6.shoppingcartservice.application.ShoppingCartService;
import com.miu.lab6.shoppingcartservice.domain.CartItem;
import com.miu.lab6.shoppingcartservice.domain.ShoppingCart;
import com.miu.lab6.shoppingcartservice.infrastructure.ProductClient;
import com.miu.lab6.shoppingcartservice.infrastructure.ProductSnapshot;
import com.miu.lab6.shoppingcartservice.infrastructure.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

  @Mock
  private ShoppingCartRepository shoppingCartRepository;

  @Mock
  private ProductClient productClient;

  @InjectMocks
  private ShoppingCartService shoppingCartService;

  @Test
  void addToShoppingCartFetchesProductDetailsAndSaves() {
    ProductSnapshot product = new ProductSnapshot();
    product.setProductNumber("P-100");
    product.setName("Book");
    product.setDescription("Desc");
    product.setPrice(new BigDecimal("25.00"));

    when(productClient.getProduct("P-100")).thenReturn(product);
    when(shoppingCartRepository.findById("CUST-1")).thenReturn(Optional.empty());
    when(shoppingCartRepository.save(any(ShoppingCart.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ShoppingCart cart = shoppingCartService.addToShoppingCart("CUST-1", "P-100", 2);

    assertThat(cart.getItems()).hasSize(1);
    CartItem item = cart.getItems().get(0);
    assertThat(item.getProductNumber()).isEqualTo("P-100");
    assertThat(item.getQuantity()).isEqualTo(2);
    assertThat(item.getPrice()).isEqualByComparingTo("25.00");
    verify(shoppingCartRepository).save(any(ShoppingCart.class));
  }

  @Test
  void getShoppingCartRefreshesPriceFromProductService() {
    ShoppingCart cart = new ShoppingCart("CUST-2");
    cart.getItems().add(new CartItem("P-200", "Old Name", "Old Desc", new BigDecimal("10.00"), 1));

    ProductSnapshot latestProduct = new ProductSnapshot();
    latestProduct.setProductNumber("P-200");
    latestProduct.setName("New Name");
    latestProduct.setDescription("New Desc");
    latestProduct.setPrice(new BigDecimal("15.00"));

    when(shoppingCartRepository.findById("CUST-2")).thenReturn(Optional.of(cart));
    when(productClient.getProduct("P-200")).thenReturn(latestProduct);
    when(shoppingCartRepository.save(any(ShoppingCart.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ShoppingCart refreshed = shoppingCartService.getShoppingCart("CUST-2");

    assertThat(refreshed.getItems()).hasSize(1);
    CartItem item = refreshed.getItems().get(0);
    assertThat(item.getPrice()).isEqualByComparingTo("15.00");
    assertThat(item.getProductName()).isEqualTo("New Name");
    verify(shoppingCartRepository).save(any(ShoppingCart.class));
  }
}
