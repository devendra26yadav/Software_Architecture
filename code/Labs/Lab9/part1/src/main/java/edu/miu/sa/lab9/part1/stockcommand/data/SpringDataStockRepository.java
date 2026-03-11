package edu.miu.sa.lab9.part1.stockcommand.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataStockRepository extends MongoRepository<StockDocument, String> {
}

