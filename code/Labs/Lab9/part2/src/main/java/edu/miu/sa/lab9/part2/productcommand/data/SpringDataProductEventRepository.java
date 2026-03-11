package edu.miu.sa.lab9.part2.productcommand.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataProductEventRepository extends MongoRepository<ProductEventDocument, String> {
    List<ProductEventDocument> findByProductNumberOrderByVersionAsc(String productNumber);
}

