package edu.miu.sa.lab9.part1;

import edu.miu.sa.lab9.part1.productcommand.application.ProductChangeType;
import edu.miu.sa.lab9.part1.productcommand.application.ProductChangedEvent;
import edu.miu.sa.lab9.part1.productcommand.application.ProductCommandService;
import edu.miu.sa.lab9.part1.productcommand.domain.Product;
import edu.miu.sa.lab9.part1.productcommand.domain.ProductStore;
import edu.miu.sa.lab9.part1.productquery.application.ProductQueryService;
import edu.miu.sa.lab9.part1.productquery.domain.ProductView;
import edu.miu.sa.lab9.part1.productquery.domain.ProductViewStore;
import edu.miu.sa.lab9.part1.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part1.stockcommand.application.StockChangedEvent;
import edu.miu.sa.lab9.part1.stockcommand.application.StockCommandService;
import edu.miu.sa.lab9.part1.stockcommand.domain.StockLevel;
import edu.miu.sa.lab9.part1.stockcommand.domain.StockStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatalogRequirementTest {

    @Test
    void queryModelReflectsProductAndStockChanges() {
        InMemoryProductStore productStore = new InMemoryProductStore();
        InMemoryStockStore stockStore = new InMemoryStockStore();
        InMemoryProductViewStore productViewStore = new InMemoryProductViewStore();
        ProductQueryService productQueryService = new ProductQueryService(productViewStore);
        DomainEventPublisher eventPublisher = event -> {
            if (event instanceof ProductChangedEvent productChangedEvent) {
                productQueryService.on(productChangedEvent);
            }
            if (event instanceof StockChangedEvent stockChangedEvent) {
                productQueryService.on(stockChangedEvent);
            }
        };

        ProductCommandService productCommandService = new ProductCommandService(productStore, eventPublisher);
        StockCommandService stockCommandService = new StockCommandService(stockStore, eventPublisher);

        productCommandService.addProduct("P-100", "Keyboard", new BigDecimal("75.00"));
        stockCommandService.upsertStock("P-100", 12);

        ProductView createdView = productQueryService.findByProductNumber("P-100");
        assertEquals("Keyboard", createdView.name());
        assertEquals(new BigDecimal("75.00"), createdView.price());
        assertEquals(12, createdView.numberInStock());

        productCommandService.updateProduct("P-100", "Mechanical Keyboard", new BigDecimal("89.99"));
        stockCommandService.upsertStock("P-100", 7);

        ProductView updatedView = productQueryService.findByProductNumber("P-100");
        assertEquals("Mechanical Keyboard", updatedView.name());
        assertEquals(new BigDecimal("89.99"), updatedView.price());
        assertEquals(7, updatedView.numberInStock());

        productCommandService.deleteProduct("P-100");
        assertEquals(List.of(), productQueryService.findAll());
    }

    @Test
    void duplicateProductNumberIsRejected() {
        InMemoryProductStore productStore = new InMemoryProductStore();
        ProductQueryService productQueryService = new ProductQueryService(new InMemoryProductViewStore());
        DomainEventPublisher eventPublisher = event -> {
            if (event instanceof ProductChangedEvent productChangedEvent) {
                productQueryService.on(productChangedEvent);
            }
        };
        ProductCommandService productCommandService = new ProductCommandService(productStore, eventPublisher);

        productCommandService.addProduct("P-200", "Mouse", new BigDecimal("25.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productCommandService.addProduct("P-200", "Mouse 2", new BigDecimal("30.00")));
        assertEquals("Product already exists: P-200", exception.getMessage());
    }

    private static final class InMemoryProductStore implements ProductStore {
        private final Map<String, Product> products = new LinkedHashMap<>();

        @Override
        public Optional<Product> findByProductNumber(String productNumber) {
            return Optional.ofNullable(products.get(productNumber));
        }

        @Override
        public Product save(Product product) {
            products.put(product.productNumber(), product);
            return product;
        }

        @Override
        public void deleteByProductNumber(String productNumber) {
            products.remove(productNumber);
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
