package edu.miu.sa.lab9.part1.productcommand.domain;

import java.util.Optional;

public interface ProductStore {
    Optional<Product> findByProductNumber(String productNumber);

    Product save(Product product);

    void deleteByProductNumber(String productNumber);
}

