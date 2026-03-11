package com.miu.lab4.webshop.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miu.lab4.webshop.product.api.AddProductRequest;
import com.miu.lab4.webshop.product.application.ProductService;
import com.miu.lab4.webshop.product.domain.Product;
import com.miu.lab4.webshop.product.infrastructure.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  @Test
  void addProductSavesWhenProductNumberDoesNotExist() {
    AddProductRequest request = new AddProductRequest();
    request.setProductNumber("P-1");
    request.setName("Book");
    request.setPrice(new BigDecimal("10.00"));
    request.setDescription("Desc");

    when(productRepository.existsById("P-1")).thenReturn(false);
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Product saved = productService.addProduct(request);

    assertThat(saved.getProductNumber()).isEqualTo("P-1");
    assertThat(saved.getName()).isEqualTo("Book");
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void addProductThrowsWhenProductAlreadyExists() {
    AddProductRequest request = new AddProductRequest();
    request.setProductNumber("P-1");
    request.setName("Book");
    request.setPrice(new BigDecimal("10.00"));
    request.setDescription("Desc");

    when(productRepository.existsById("P-1")).thenReturn(true);

    assertThatThrownBy(() -> productService.addProduct(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Product already exists");
  }

  @Test
  void getProductThrowsWhenMissing() {
    String productNumber = UUID.randomUUID().toString();
    when(productRepository.findById(productNumber)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.getProduct(productNumber))
        .isInstanceOf(java.util.NoSuchElementException.class)
        .hasMessageContaining("Product not found");
  }
}
