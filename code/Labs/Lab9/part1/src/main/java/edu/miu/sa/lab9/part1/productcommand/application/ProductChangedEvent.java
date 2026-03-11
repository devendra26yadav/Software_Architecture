package edu.miu.sa.lab9.part1.productcommand.application;

import java.math.BigDecimal;

public record ProductChangedEvent(
        String productNumber,
        String name,
        BigDecimal price,
        ProductChangeType changeType
) {
}

