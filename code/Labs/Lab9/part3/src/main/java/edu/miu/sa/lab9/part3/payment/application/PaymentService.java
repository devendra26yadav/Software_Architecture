package edu.miu.sa.lab9.part3.payment.application;

import edu.miu.sa.lab9.part3.payment.domain.Payment;
import edu.miu.sa.lab9.part3.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part3.payment.domain.PaymentStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentStore paymentStore;

    public PaymentService(PaymentStore paymentStore) {
        this.paymentStore = paymentStore;
    }

    public Payment process(String bookingId, BigDecimal amount, boolean failPayment) {
        PaymentStatus status = failPayment ? PaymentStatus.FAILED : PaymentStatus.COMPLETED;
        Payment payment = paymentStore.save(new Payment(bookingId, amount, status));
        if (failPayment) {
            throw new PaymentFailedException("Payment failed for booking " + bookingId);
        }
        return payment;
    }

    public Payment refund(String bookingId) {
        Payment existing = getByBookingId(bookingId);
        return paymentStore.save(new Payment(existing.bookingId(), existing.amount(), PaymentStatus.REFUNDED));
    }

    public Payment getByBookingId(String bookingId) {
        return paymentStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Payment not found: " + bookingId));
    }
}

