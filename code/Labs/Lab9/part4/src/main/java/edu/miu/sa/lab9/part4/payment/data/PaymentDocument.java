package edu.miu.sa.lab9.part4.payment.data;

import edu.miu.sa.lab9.part4.payment.domain.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("part4_payments")
public class PaymentDocument {

    @Id
    private String bookingId;
    private BigDecimal amount;
    private PaymentStatus status;

    public PaymentDocument() {
    }

    public PaymentDocument(String bookingId, BigDecimal amount, PaymentStatus status) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}

