package edu.miu.sa.lab9.part1.productcommand.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataProductRepository extends MongoRepository<ProductDocument, String> {
}

