package edu.miu.sa.lab9.part2.productcommand.application;

import edu.miu.sa.lab9.part2.productcommand.domain.ProductChangeType;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventRecord;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventStore;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductSnapshot;
import edu.miu.sa.lab9.part2.shared.DomainEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class ProductCommandService {

    private final ProductEventStore productEventStore;
    private final DomainEventPublisher domainEventPublisher;

    public ProductCommandService(ProductEventStore productEventStore, DomainEventPublisher domainEventPublisher) {
        this.productEventStore = productEventStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    public ProductSnapshot addProduct(String productNumber, String name, BigDecimal price) {
        ProductSnapshot snapshot = load(productNumber);
        if (snapshot.version() > 0 && !snapshot.deleted()) {
            throw new IllegalArgumentException("Product already exists: " + productNumber);
        }
        ProductEventRecord event = append(productNumber, ProductChangeType.CREATED, name, price, snapshot.version() + 1);
        ProductSnapshot updated = apply(snapshot, event);
        publishProjection(updated, ProductChangeType.CREATED);
        return updated;
    }

    public ProductSnapshot updateProduct(String productNumber, String name, BigDecimal price) {
        ProductSnapshot snapshot = load(productNumber);
        requireActive(snapshot);
        ProductEventRecord event = append(productNumber, ProductChangeType.UPDATED, name, price, snapshot.version() + 1);
        ProductSnapshot updated = apply(snapshot, event);
        publishProjection(updated, ProductChangeType.UPDATED);
        return updated;
    }

    public void deleteProduct(String productNumber) {
        ProductSnapshot snapshot = load(productNumber);
        requireActive(snapshot);
        ProductEventRecord event = append(productNumber, ProductChangeType.DELETED, snapshot.name(), snapshot.price(), snapshot.version() + 1);
        ProductSnapshot updated = apply(snapshot, event);
        publishProjection(updated, ProductChangeType.DELETED);
    }

    public ProductSnapshot getCurrentProduct(String productNumber) {
        ProductSnapshot snapshot = load(productNumber);
        requireActive(snapshot);
        return snapshot;
    }

    private ProductEventRecord append(String productNumber, ProductChangeType changeType, String name, BigDecimal price, long version) {
        return productEventStore.append(new ProductEventRecord(
                productNumber,
                changeType,
                name,
                price,
                version,
                Instant.now()
        ));
    }

    private ProductSnapshot load(String productNumber) {
        List<ProductEventRecord> eventHistory = productEventStore.findByProductNumber(productNumber);
        ProductSnapshot snapshot = ProductSnapshot.empty(productNumber);
        for (ProductEventRecord event : eventHistory) {
            snapshot = apply(snapshot, event);
        }
        return snapshot;
    }

    private ProductSnapshot apply(ProductSnapshot current, ProductEventRecord event) {
        return switch (event.changeType()) {
            case CREATED, UPDATED -> new ProductSnapshot(
                    event.productNumber(),
                    event.name(),
                    event.price(),
                    event.version(),
                    false
            );
            case DELETED -> new ProductSnapshot(
                    event.productNumber(),
                    current.name(),
                    current.price(),
                    event.version(),
                    true
            );
        };
    }

    private void publishProjection(ProductSnapshot snapshot, ProductChangeType changeType) {
        domainEventPublisher.publish(new ProductProjectionChangedEvent(
                snapshot.productNumber(),
                snapshot.name(),
                snapshot.price(),
                changeType
        ));
    }

    private void requireActive(ProductSnapshot snapshot) {
        if (snapshot.version() == 0 || snapshot.deleted()) {
            throw new IllegalArgumentException("Product not found: " + snapshot.productNumber());
        }
    }
}

