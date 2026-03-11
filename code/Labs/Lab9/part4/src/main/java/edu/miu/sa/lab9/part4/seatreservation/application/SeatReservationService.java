package edu.miu.sa.lab9.part4.seatreservation.application;

import edu.miu.sa.lab9.part4.booking.events.BookingRequestedEvent;
import edu.miu.sa.lab9.part4.booking.events.PaymentFailedEvent;
import edu.miu.sa.lab9.part4.booking.events.SeatReservedEvent;
import edu.miu.sa.lab9.part4.booking.events.TicketIssueFailedEvent;
import edu.miu.sa.lab9.part4.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part4.seatreservation.domain.SeatReservation;
import edu.miu.sa.lab9.part4.seatreservation.domain.SeatReservationStore;
import edu.miu.sa.lab9.part4.shared.DomainEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeatReservationService {

    private final SeatReservationStore seatReservationStore;
    private final DomainEventPublisher domainEventPublisher;

    public SeatReservationService(SeatReservationStore seatReservationStore, DomainEventPublisher domainEventPublisher) {
        this.seatReservationStore = seatReservationStore;
        this.domainEventPublisher = domainEventPublisher;
    }

    @EventListener
    public void on(BookingRequestedEvent event) {
        SeatReservation seatReservation = seatReservationStore.save(new SeatReservation(
                event.request().bookingId(),
                event.request().flightNumber(),
                event.request().seatNumber(),
                event.request().customerName(),
                ReservationStatus.RESERVED
        ));
        domainEventPublisher.publish(new SeatReservedEvent(event.request()));
    }

    @EventListener
    public void on(PaymentFailedEvent event) {
        cancel(event.request().bookingId());
    }

    @EventListener
    public void on(TicketIssueFailedEvent event) {
        cancel(event.request().bookingId());
    }

    public SeatReservation cancel(String bookingId) {
        SeatReservation existing = getByBookingId(bookingId);
        return seatReservationStore.save(new SeatReservation(
                existing.bookingId(),
                existing.flightNumber(),
                existing.seatNumber(),
                existing.customerName(),
                ReservationStatus.CANCELLED
        ));
    }

    public SeatReservation getByBookingId(String bookingId) {
        return seatReservationStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Reservation not found: " + bookingId));
    }

    public Optional<SeatReservation> findByBookingId(String bookingId) {
        return seatReservationStore.findByBookingId(bookingId);
    }
}

