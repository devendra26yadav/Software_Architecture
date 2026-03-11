package edu.miu.sa.lab9.part4.payment.application;

import edu.miu.sa.lab9.part4.booking.application.BookingRequest;
import edu.miu.sa.lab9.part4.booking.events.PaymentCompletedEvent;
import edu.miu.sa.lab9.part4.booking.events.PaymentFailedEvent;
import edu.miu.sa.lab9.part4.booking.events.SeatReservedEvent;
import edu.miu.sa.lab9.part4.booking.events.TicketIssueFailedEvent;
import edu.miu.sa.lab9.part4.payment.domain.Payment;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStore;
import edu.miu.sa.lab9.part4.shared.DomainEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentStore paymentStore;
    private final DomainEventPublisher domainEventPublisher;

    public PaymentService(PaymentStore paymentStore, DomainEventPublisher domainEventPublisher) {
        this.paymentStore = paymentStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    @EventListener
    public void on(SeatReservedEvent event) {
        BookingRequest request = event.request();
        Payment payment = paymentStore.save(new Payment(
                request.bookingId(),
                request.amount(),
                request.failPayment() ? PaymentStatus.FAILED : PaymentStatus.COMPLETED
        ));
        if (payment.status() == PaymentStatus.FAILED) {
            domainEventPublisher.publish(new PaymentFailedEvent(request, "Payment failed for booking " + request.bookingId()));
            return;
        }
        domainEventPublisher.publish(new PaymentCompletedEvent(request));
    }

    @EventListener
    public void on(TicketIssueFailedEvent event) {
        refund(event.request().bookingId());
    }

    public Payment refund(String bookingId) {
        Payment existing = getByBookingId(bookingId);
        return paymentStore.save(new Payment(existing.bookingId(), existing.amount(), PaymentStatus.REFUNDED));
    }

    public Payment getByBookingId(String bookingId) {
        return paymentStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Payment not found: " + bookingId));
    }

    public Optional<Payment> findByBookingId(String bookingId) {
        return paymentStore.findByBookingId(bookingId);
    }
}

