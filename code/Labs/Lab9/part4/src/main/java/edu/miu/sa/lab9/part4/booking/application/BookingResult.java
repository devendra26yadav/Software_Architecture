package edu.miu.sa.lab9.part4.booking.application;

import edu.miu.sa.lab9.part4.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part4.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStatus;

public record BookingResult(
        String bookingId,
        BookingStatus bookingStatus,
        String message,
        ReservationStatus reservationStatus,
        PaymentStatus paymentStatus,
        TicketStatus ticketStatus
) {
}

