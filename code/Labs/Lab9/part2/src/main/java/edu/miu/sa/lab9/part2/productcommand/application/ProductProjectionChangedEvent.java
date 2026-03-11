package edu.miu.sa.lab9.part2.productcommand.application;

import edu.miu.sa.lab9.part2.productcommand.domain.ProductChangeType;

import java.math.BigDecimal;

public record ProductProjectionChangedEvent(
        String productNumber,
        String name,
        BigDecimal price,
        ProductChangeType changeType
) {
}

