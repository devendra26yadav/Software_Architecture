package com.miu.lab6.shoppingcartservice.infrastructure;

import com.miu.lab6.shoppingcartservice.domain.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
}
