package edu.miu.sa.lab9.part3.ticket.application;

import edu.miu.sa.lab9.part3.ticket.domain.Ticket;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStatus;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStore;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketStore ticketStore;

    public TicketService(TicketStore ticketStore) {
        this.ticketStore = ticketStore;
    }

    public Ticket issue(String bookingId, String flightNumber, String seatNumber, String customerName, boolean failTicketIssue) {
        TicketStatus status = failTicketIssue ? TicketStatus.FAILED : TicketStatus.ISSUED;
        Ticket ticket = ticketStore.save(new Ticket(bookingId, flightNumber, seatNumber, customerName, status));
        if (failTicketIssue) {
            throw new TicketIssueFailedException("Ticket issue failed for booking " + bookingId);
        }
        return ticket;
    }

    public Ticket getByBookingId(String bookingId) {
        return ticketStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Ticket not found: " + bookingId));
    }
}

