package edu.miu.sa.lab9.part2.productcommand.domain;

import java.math.BigDecimal;

public record ProductSnapshot(
        String productNumber,
        String name,
        BigDecimal price,
        long version,
        boolean deleted
) {
    public static ProductSnapshot empty(String productNumber) {
        return new ProductSnapshot(productNumber, null, null, 0, false);
    }
}

