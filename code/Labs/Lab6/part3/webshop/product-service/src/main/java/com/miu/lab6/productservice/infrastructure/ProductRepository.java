package com.miu.lab6.productservice.infrastructure;

import com.miu.lab6.productservice.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
