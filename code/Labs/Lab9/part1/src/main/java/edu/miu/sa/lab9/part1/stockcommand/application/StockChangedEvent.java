package edu.miu.sa.lab9.part1.stockcommand.application;

public record StockChangedEvent(String productNumber, int quantity) {
}

