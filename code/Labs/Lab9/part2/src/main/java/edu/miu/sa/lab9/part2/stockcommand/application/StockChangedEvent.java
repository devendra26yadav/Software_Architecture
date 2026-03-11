package edu.miu.sa.lab9.part2.stockcommand.application;

public record StockChangedEvent(String productNumber, int quantity) {
}

