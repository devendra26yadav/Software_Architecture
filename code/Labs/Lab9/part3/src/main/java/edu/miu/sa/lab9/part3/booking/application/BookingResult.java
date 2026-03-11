package edu.miu.sa.lab9.part3.booking.application;

import edu.miu.sa.lab9.part3.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part3.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStatus;

public record BookingResult(
        String bookingId,
        BookingStatus bookingStatus,
        String message,
        ReservationStatus reservationStatus,
        PaymentStatus paymentStatus,
        TicketStatus ticketStatus
) {
}

