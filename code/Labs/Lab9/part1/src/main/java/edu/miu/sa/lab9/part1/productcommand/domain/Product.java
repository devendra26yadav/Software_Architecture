package edu.miu.sa.lab9.part1.productcommand.domain;

import java.math.BigDecimal;

public record Product(String productNumber, String name, BigDecimal price) {
}

