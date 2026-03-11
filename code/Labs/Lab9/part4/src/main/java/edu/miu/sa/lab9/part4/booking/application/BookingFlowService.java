package edu.miu.sa.lab9.part4.booking.application;

import edu.miu.sa.lab9.part4.booking.events.BookingRequestedEvent;
import edu.miu.sa.lab9.part4.payment.application.PaymentService;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part4.seatreservation.application.SeatReservationService;
import edu.miu.sa.lab9.part4.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part4.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part4.ticket.application.TicketService;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStatus;
import org.springframework.stereotype.Service;

@Service
public class BookingFlowService {

    private final DomainEventPublisher domainEventPublisher;
    private final SeatReservationService seatReservationService;
    private final PaymentService paymentService;
    private final TicketService ticketService;

    public BookingFlowService(
            DomainEventPublisher domainEventPublisher,
            SeatReservationService seatReservationService,
            PaymentService paymentService,
            TicketService ticketService
    ) {
        this.domainEventPublisher = domainEventPublisher;
        this.seatReservationService = seatReservationService;
        this.paymentService = paymentService;
        this.ticketService = ticketService;
    }

    public BookingResult startBooking(BookingRequest request) {
        domainEventPublisher.publish(new BookingRequestedEvent(request));
        ReservationStatus reservationStatus = seatReservationService.findByBookingId(request.bookingId())
                .map(seatReservation -> seatReservation.status())
                .orElse(null);
        PaymentStatus paymentStatus = paymentService.findByBookingId(request.bookingId())
                .map(payment -> payment.status())
                .orElse(null);
        TicketStatus ticketStatus = ticketService.findByBookingId(request.bookingId())
                .map(ticket -> ticket.status())
                .orElse(null);

        BookingStatus bookingStatus = BookingStatus.COMPLETED;
        String message = "Booking completed successfully";
        if (paymentStatus == PaymentStatus.FAILED) {
            bookingStatus = BookingStatus.PAYMENT_FAILED;
            message = "Payment failed for booking " + request.bookingId();
        } else if (ticketStatus == TicketStatus.FAILED) {
            bookingStatus = BookingStatus.TICKET_FAILED;
            message = "Ticket issue failed for booking " + request.bookingId();
        }
        return new BookingResult(request.bookingId(), bookingStatus, message, reservationStatus, paymentStatus, ticketStatus);
    }
}

