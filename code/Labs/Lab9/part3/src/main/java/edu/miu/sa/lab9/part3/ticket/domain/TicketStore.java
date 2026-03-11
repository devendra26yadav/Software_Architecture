package edu.miu.sa.lab9.part3.ticket.domain;

import java.util.Optional;

public interface TicketStore {
    Ticket save(Ticket ticket);

    Optional<Ticket> findByBookingId(String bookingId);
}

