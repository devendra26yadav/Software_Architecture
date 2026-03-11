package edu.miu.sa.lab9.part1.productquery.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataProductViewRepository extends MongoRepository<ProductViewDocument, String> {
}

