package edu.miu.sa.lab9.part2.stockcommand.application;

import edu.miu.sa.lab9.part2.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockLevel;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockStore;
import org.springframework.stereotype.Service;

@Service
public class StockCommandService {

    private final StockStore stockStore;
    private final DomainEventPublisher domainEventPublisher;

    public StockCommandService(StockStore stockStore, DomainEventPublisher domainEventPublisher) {
        this.stockStore = stockStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    public StockLevel upsertStock(String productNumber, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        StockLevel stockLevel = stockStore.save(new StockLevel(productNumber, quantity));
        domainEventPublisher.publish(new StockChangedEvent(stockLevel.productNumber(), stockLevel.quantity()));
        return stockLevel;
    }

    public void deleteStock(String productNumber) {
        stockStore.deleteByProductNumber(productNumber);
        domainEventPublisher.publish(new StockChangedEvent(productNumber, 0));
    }
}

