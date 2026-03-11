package edu.miu.sa.lab9.part3.payment.application;

public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String message) {
        super(message);
    }
}

