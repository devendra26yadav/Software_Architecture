package edu.miu.sa.lab9.part4.payment.domain;

import java.math.BigDecimal;

public record Payment(String bookingId, BigDecimal amount, PaymentStatus status) {
}

