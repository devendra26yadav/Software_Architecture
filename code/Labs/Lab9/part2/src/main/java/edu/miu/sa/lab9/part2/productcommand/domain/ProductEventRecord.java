package edu.miu.sa.lab9.part2.productcommand.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductEventRecord(
        String productNumber,
        ProductChangeType changeType,
        String name,
        BigDecimal price,
        long version,
        Instant createdAt
) {
}

