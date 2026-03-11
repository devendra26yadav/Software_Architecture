package edu.miu.sa.lab9.part1.productquery.domain;

import java.util.List;
import java.util.Optional;

public interface ProductViewStore {
    Optional<ProductView> findByProductNumber(String productNumber);

    ProductView save(ProductView productView);

    void deleteByProductNumber(String productNumber);

    List<ProductView> findAll();
}

