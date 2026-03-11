package edu.miu.sa.lab11.kafka.domain;

import java.math.BigDecimal;

public record Order(
        String orderNumber,
        String customerName,
        String customerCountry,
        BigDecimal amount,
        OrderStatus status
) {
}
