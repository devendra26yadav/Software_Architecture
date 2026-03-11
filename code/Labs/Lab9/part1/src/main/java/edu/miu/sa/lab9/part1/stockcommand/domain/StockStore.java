package edu.miu.sa.lab9.part1.stockcommand.domain;

import java.util.Optional;

public interface StockStore {
    Optional<StockLevel> findByProductNumber(String productNumber);

    StockLevel save(StockLevel stockLevel);

    void deleteByProductNumber(String productNumber);
}

