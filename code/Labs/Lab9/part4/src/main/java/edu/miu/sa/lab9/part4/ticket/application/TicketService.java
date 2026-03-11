package edu.miu.sa.lab9.part4.ticket.application;

import edu.miu.sa.lab9.part4.booking.application.BookingRequest;
import edu.miu.sa.lab9.part4.booking.events.PaymentCompletedEvent;
import edu.miu.sa.lab9.part4.booking.events.TicketIssueFailedEvent;
import edu.miu.sa.lab9.part4.booking.events.TicketIssuedEvent;
import edu.miu.sa.lab9.part4.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part4.ticket.domain.Ticket;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStatus;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStore;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {

    private final TicketStore ticketStore;
    private final DomainEventPublisher domainEventPublisher;

    public TicketService(TicketStore ticketStore, DomainEventPublisher domainEventPublisher) {
        this.ticketStore = ticketStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    @EventListener
    public void on(PaymentCompletedEvent event) {
        BookingRequest request = event.request();
        Ticket ticket = ticketStore.save(new Ticket(
                request.bookingId(),
                request.flightNumber(),
                request.seatNumber(),
                request.customerName(),
                request.failTicketIssue() ? TicketStatus.FAILED : TicketStatus.ISSUED
        ));
        if (ticket.status() == TicketStatus.FAILED) {
            domainEventPublisher.publish(new TicketIssueFailedEvent(request, "Ticket issue failed for booking " + request.bookingId()));
            return;
        }
        domainEventPublisher.publish(new TicketIssuedEvent(request));
    }

    public Ticket getByBookingId(String bookingId) {
        return ticketStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Ticket not found: " + bookingId));
    }

    public Optional<Ticket> findByBookingId(String bookingId) {
        return ticketStore.findByBookingId(bookingId);
    }
}

