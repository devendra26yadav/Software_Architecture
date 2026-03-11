package com.miu.lab4.webshop.shopping.infrastructure;

import com.miu.lab4.webshop.shopping.domain.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
}
