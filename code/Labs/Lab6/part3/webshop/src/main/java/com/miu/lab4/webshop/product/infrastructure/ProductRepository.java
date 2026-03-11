package com.miu.lab4.webshop.product.infrastructure;

import com.miu.lab4.webshop.product.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
