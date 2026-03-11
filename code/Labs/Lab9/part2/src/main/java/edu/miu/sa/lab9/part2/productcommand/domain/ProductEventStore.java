package edu.miu.sa.lab9.part2.productcommand.domain;

import java.util.List;

public interface ProductEventStore {
    List<ProductEventRecord> findByProductNumber(String productNumber);

    ProductEventRecord append(ProductEventRecord eventRecord);
}

