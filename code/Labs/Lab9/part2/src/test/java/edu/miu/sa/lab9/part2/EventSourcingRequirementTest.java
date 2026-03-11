package edu.miu.sa.lab9.part2;

import edu.miu.sa.lab9.part2.productcommand.application.ProductCommandService;
import edu.miu.sa.lab9.part2.productcommand.application.ProductProjectionChangedEvent;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductChangeType;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventRecord;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventStore;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductSnapshot;
import edu.miu.sa.lab9.part2.productquery.application.ProductQueryService;
import edu.miu.sa.lab9.part2.productquery.domain.ProductView;
import edu.miu.sa.lab9.part2.productquery.domain.ProductViewStore;
import edu.miu.sa.lab9.part2.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part2.stockcommand.application.StockChangedEvent;
import edu.miu.sa.lab9.part2.stockcommand.application.StockCommandService;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockLevel;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventSourcingRequirementTest {

    @Test
    void productCommandsAreStoredAsEventsAndProjectedToQueryModel() {
        InMemoryProductEventStore productEventStore = new InMemoryProductEventStore();
        InMemoryStockStore stockStore = new InMemoryStockStore();
        InMemoryProductViewStore viewStore = new InMemoryProductViewStore();
        ProductQueryService productQueryService = new ProductQueryService(viewStore);
        DomainEventPublisher eventPublisher = event -> {
            if (event instanceof ProductProjectionChangedEvent projectionChangedEvent) {
                productQueryService.on(projectionChangedEvent);
            }
            if (event instanceof StockChangedEvent stockChangedEvent) {
                productQueryService.on(stockChangedEvent);
            }
        };

        ProductCommandService productCommandService = new ProductCommandService(productEventStore, eventPublisher);
        StockCommandService stockCommandService = new StockCommandService(stockStore, eventPublisher);

        ProductSnapshot created = productCommandService.addProduct("P-300", "Monitor", new BigDecimal("210.00"));
        ProductSnapshot updated = productCommandService.updateProduct("P-300", "4K Monitor", new BigDecimal("299.99"));
        stockCommandService.upsertStock("P-300", 15);

        ProductView view = productQueryService.findByProductNumber("P-300");
        assertEquals("4K Monitor", view.name());
        assertEquals(new BigDecimal("299.99"), view.price());
        assertEquals(15, view.numberInStock());

        List<ProductEventRecord> events = productEventStore.findByProductNumber("P-300");
        assertEquals(2, events.size());
        assertEquals(ProductChangeType.CREATED, events.get(0).changeType());
        assertEquals(1, events.get(0).version());
        assertEquals(ProductChangeType.UPDATED, events.get(1).changeType());
        assertEquals(2, events.get(1).version());
        assertEquals(2, updated.version());
        assertEquals("Monitor", created.name());
    }

    @Test
    void deletingAProductRemovesItFromTheQueryModel() {
        InMemoryProductEventStore productEventStore = new InMemoryProductEventStore();
        ProductQueryService productQueryService = new ProductQueryService(new InMemoryProductViewStore());
        DomainEventPublisher eventPublisher = event -> {
            if (event instanceof ProductProjectionChangedEvent projectionChangedEvent) {
                productQueryService.on(projectionChangedEvent);
            }
        };

        ProductCommandService productCommandService = new ProductCommandService(productEventStore, eventPublisher);
        productCommandService.addProduct("P-301", "Tablet", new BigDecimal("320.00"));
        productCommandService.deleteProduct("P-301");

        assertEquals(ProductChangeType.DELETED, productEventStore.findByProductNumber("P-301").getLast().changeType());
        assertThrows(IllegalArgumentException.class, () -> productQueryService.findByProductNumber("P-301"));
    }

    private static final class InMemoryProductEventStore implements ProductEventStore {
        private final Map<String, List<ProductEventRecord>> eventStore = new LinkedHashMap<>();

        @Override
        public List<ProductEventRecord> findByProductNumber(String productNumber) {
            return eventStore.getOrDefault(productNumber, List.of()).stream()
                    .sorted(Comparator.comparingLong(ProductEventRecord::version))
                    .toList();
        }

        @Override
        public ProductEventRecord append(ProductEventRecord eventRecord) {
            eventStore.computeIfAbsent(eventRecord.productNumber(), key -> new ArrayList<>())
                    .add(new ProductEventRecord(
                            eventRecord.productNumber(),
                            eventRecord.changeType(),
                            eventRecord.name(),
                            eventRecord.price(),
                            eventRecord.version(),
                            Optional.ofNullable(eventRecord.createdAt()).orElse(Instant.now())
                    ));
            return findByProductNumber(eventRecord.productNumber()).getLast();
        }
    }

    private static final class InMemoryStockStore implements StockStore {
        private final Map<String, StockLevel> stockLevels = new LinkedHashMap<>();

        @Override
        public Optional<StockLevel> findByProductNumber(String productNumber) {
            return Optional.ofNullable(stockLevels.get(productNumber));
        }

        @Override
        public StockLevel save(StockLevel stockLevel) {
            stockLevels.put(stockLevel.productNumber(), stockLevel);
            return stockLevel;
        }

        @Override
        public void deleteByProductNumber(String productNumber) {
            stockLevels.remove(productNumber);
        }
    }

    private static final class InMemoryProductViewStore implements ProductViewStore {
        private final Map<String, ProductView> productViews = new LinkedHashMap<>();

        @Override
        public Optional<ProductView> findByProductNumber(String productNumber) {
            return Optional.ofNullable(productViews.get(productNumber));
        }

        @Override
        public ProductView save(ProductView productView) {
            productViews.put(productView.productNumber(), productView);
            return productView;
        }

        @Override
        public void deleteByProductNumber(String productNumber) {
            productViews.remove(productNumber);
        }

        @Override
        public List<ProductView> findAll() {
            return new ArrayList<>(productViews.values());
        }
    }
}
