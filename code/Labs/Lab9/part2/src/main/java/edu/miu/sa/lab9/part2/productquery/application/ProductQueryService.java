package edu.miu.sa.lab9.part2.productquery.application;

import edu.miu.sa.lab9.part2.productcommand.application.ProductProjectionChangedEvent;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductChangeType;
import edu.miu.sa.lab9.part2.productquery.domain.ProductView;
import edu.miu.sa.lab9.part2.productquery.domain.ProductViewStore;
import edu.miu.sa.lab9.part2.stockcommand.application.StockChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    private final ProductViewStore productViewStore;

    public ProductQueryService(ProductViewStore productViewStore) {
        this.productViewStore = productViewStore;
    }

    @EventListener
    public void on(ProductProjectionChangedEvent event) {
        if (event.changeType() == ProductChangeType.DELETED) {
            productViewStore.deleteByProductNumber(event.productNumber());
            return;
        }
        ProductView current = productViewStore.findByProductNumber(event.productNumber())
                .orElse(new ProductView(event.productNumber(), null, null, 0));
        productViewStore.save(new ProductView(
                event.productNumber(),
                event.name(),
                event.price(),
                current.numberInStock()
        ));
    }

    @EventListener
    public void on(StockChangedEvent event) {
        ProductView current = productViewStore.findByProductNumber(event.productNumber())
                .orElse(new ProductView(event.productNumber(), null, null, 0));
        productViewStore.save(new ProductView(
                event.productNumber(),
                current.name(),
                current.price(),
                event.quantity()
        ));
    }

    public List<ProductView> findAll() {
        return productViewStore.findAll();
    }

    public ProductView findByProductNumber(String productNumber) {
        return productViewStore.findByProductNumber(productNumber).orElseThrow(() ->
                new IllegalArgumentException("Product not found in query model: " + productNumber));
    }
}

