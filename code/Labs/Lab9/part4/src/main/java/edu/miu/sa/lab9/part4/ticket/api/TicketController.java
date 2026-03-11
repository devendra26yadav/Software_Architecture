package edu.miu.sa.lab9.part4.ticket.api;

import edu.miu.sa.lab9.part4.ticket.application.TicketService;
import edu.miu.sa.lab9.part4.ticket.domain.Ticket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{bookingId}")
    public Ticket findByBookingId(@PathVariable String bookingId) {
        return ticketService.getByBookingId(bookingId);
    }
}

