package edu.miu.sa.lab9.part3.booking.application;

import edu.miu.sa.lab9.part3.payment.application.PaymentFailedException;
import edu.miu.sa.lab9.part3.payment.application.PaymentService;
import edu.miu.sa.lab9.part3.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part3.seatreservation.application.SeatReservationService;
import edu.miu.sa.lab9.part3.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part3.ticket.application.TicketIssueFailedException;
import edu.miu.sa.lab9.part3.ticket.application.TicketService;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStatus;
import org.springframework.stereotype.Service;

@Service
public class BookingSagaOrchestrator {

    private final SeatReservationService seatReservationService;
    private final PaymentService paymentService;
    private final TicketService ticketService;

    public BookingSagaOrchestrator(
            SeatReservationService seatReservationService,
            PaymentService paymentService,
            TicketService ticketService
    ) {
        this.seatReservationService = seatReservationService;
        this.paymentService = paymentService;
        this.ticketService = ticketService;
    }

    public BookingResult book(BookingRequest request) {
        seatReservationService.reserve(request.bookingId(), request.flightNumber(), request.seatNumber(), request.customerName());
        try {
            paymentService.process(request.bookingId(), request.amount(), request.failPayment());
        } catch (PaymentFailedException exception) {
            seatReservationService.cancel(request.bookingId());
            return new BookingResult(
                    request.bookingId(),
                    BookingStatus.PAYMENT_FAILED,
                    exception.getMessage(),
                    ReservationStatus.CANCELLED,
                    PaymentStatus.FAILED,
                    null
            );
        }

        try {
            ticketService.issue(request.bookingId(), request.flightNumber(), request.seatNumber(), request.customerName(), request.failTicketIssue());
        } catch (TicketIssueFailedException exception) {
            paymentService.refund(request.bookingId());
            seatReservationService.cancel(request.bookingId());
            return new BookingResult(
                    request.bookingId(),
                    BookingStatus.TICKET_FAILED,
                    exception.getMessage(),
                    ReservationStatus.CANCELLED,
                    PaymentStatus.REFUNDED,
                    TicketStatus.FAILED
            );
        }

        return new BookingResult(
                request.bookingId(),
                BookingStatus.COMPLETED,
                "Booking completed successfully",
                ReservationStatus.RESERVED,
                PaymentStatus.COMPLETED,
                TicketStatus.ISSUED
        );
    }
}

