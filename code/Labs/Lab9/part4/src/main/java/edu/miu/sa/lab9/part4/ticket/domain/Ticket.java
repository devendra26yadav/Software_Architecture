package edu.miu.sa.lab9.part4.ticket.domain;

public record Ticket(
        String bookingId,
        String flightNumber,
        String seatNumber,
        String customerName,
        TicketStatus status
) {
}

