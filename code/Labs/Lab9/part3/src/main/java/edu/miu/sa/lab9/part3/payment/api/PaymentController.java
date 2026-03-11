package edu.miu.sa.lab9.part3.payment.api;

import edu.miu.sa.lab9.part3.payment.application.PaymentService;
import edu.miu.sa.lab9.part3.payment.domain.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{bookingId}")
    public Payment findByBookingId(@PathVariable String bookingId) {
        return paymentService.getByBookingId(bookingId);
    }
}

