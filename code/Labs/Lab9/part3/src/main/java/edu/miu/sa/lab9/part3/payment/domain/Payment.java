package edu.miu.sa.lab9.part3.payment.domain;

import java.math.BigDecimal;

public record Payment(String bookingId, BigDecimal amount, PaymentStatus status) {
}

