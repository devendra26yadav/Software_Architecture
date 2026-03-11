package edu.miu.sa.lab9.part3.ticket.domain;

public record Ticket(
        String bookingId,
        String flightNumber,
        String seatNumber,
        String customerName,
        TicketStatus status
) {
}

