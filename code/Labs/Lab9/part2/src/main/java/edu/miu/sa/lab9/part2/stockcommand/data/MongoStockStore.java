package edu.miu.sa.lab9.part2.stockcommand.data;

import edu.miu.sa.lab9.part2.stockcommand.domain.StockLevel;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoStockStore implements StockStore {

    private final SpringDataStockRepository repository;

    public MongoStockStore(SpringDataStockRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<StockLevel> findByProductNumber(String productNumber) {
        return repository.findById(productNumber).map(this::toDomain);
    }

    @Override
    public StockLevel save(StockLevel stockLevel) {
        return toDomain(repository.save(new StockDocument(stockLevel.productNumber(), stockLevel.quantity())));
    }

    @Override
    public void deleteByProductNumber(String productNumber) {
        repository.deleteById(productNumber);
    }

    private StockLevel toDomain(StockDocument document) {
        return new StockLevel(document.getProductNumber(), document.getQuantity());
    }
}

