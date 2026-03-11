package edu.miu.sa.lab9.part4.booking.events;

import edu.miu.sa.lab9.part4.booking.application.BookingRequest;

public record TicketIssuedEvent(BookingRequest request) {
}

