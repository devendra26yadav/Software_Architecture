package edu.miu.sa.lab9.part2.productquery.domain;

import java.math.BigDecimal;

public record ProductView(String productNumber, String name, BigDecimal price, int numberInStock) {
}

