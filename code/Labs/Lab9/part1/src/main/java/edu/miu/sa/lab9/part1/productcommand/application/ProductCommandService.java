package edu.miu.sa.lab9.part1.productcommand.application;

import edu.miu.sa.lab9.part1.productcommand.domain.Product;
import edu.miu.sa.lab9.part1.productcommand.domain.ProductStore;
import edu.miu.sa.lab9.part1.shared.DomainEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductCommandService {

    private final ProductStore productStore;
    private final DomainEventPublisher domainEventPublisher;

    public ProductCommandService(ProductStore productStore, DomainEventPublisher domainEventPublisher) {
        this.productStore = productStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Product addProduct(String productNumber, String name, BigDecimal price) {
        productStore.findByProductNumber(productNumber).ifPresent(existing -> {
            throw new IllegalArgumentException("Product already exists: " + productNumber);
        });
        Product product = new Product(productNumber, name, price);
        Product savedProduct = productStore.save(product);
        publishUpsert(savedProduct);
        return savedProduct;
    }

    public Product updateProduct(String productNumber, String name, BigDecimal price) {
        productStore.findByProductNumber(productNumber).orElseThrow(() ->
                new IllegalArgumentException("Product not found: " + productNumber));
        Product savedProduct = productStore.save(new Product(productNumber, name, price));
        publishUpsert(savedProduct);
        return savedProduct;
    }

    public void deleteProduct(String productNumber) {
        productStore.findByProductNumber(productNumber).orElseThrow(() ->
                new IllegalArgumentException("Product not found: " + productNumber));
        productStore.deleteByProductNumber(productNumber);
        domainEventPublisher.publish(new ProductChangedEvent(productNumber, null, null, ProductChangeType.DELETED));
    }

    private void publishUpsert(Product product) {
        domainEventPublisher.publish(new ProductChangedEvent(
                product.productNumber(),
                product.name(),
                product.price(),
                ProductChangeType.UPSERT
        ));
    }
}

