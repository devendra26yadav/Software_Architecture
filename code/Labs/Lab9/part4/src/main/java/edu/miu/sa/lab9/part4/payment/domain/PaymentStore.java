package edu.miu.sa.lab9.part4.payment.domain;

import java.util.Optional;

public interface PaymentStore {
    Payment save(Payment payment);

    Optional<Payment> findByBookingId(String bookingId);
}

