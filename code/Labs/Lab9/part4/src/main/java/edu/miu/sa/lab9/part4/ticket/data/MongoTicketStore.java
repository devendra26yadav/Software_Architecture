package edu.miu.sa.lab9.part4.ticket.data;

import edu.miu.sa.lab9.part4.ticket.domain.Ticket;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoTicketStore implements TicketStore {

    private final SpringDataTicketRepository repository;

    public MongoTicketStore(SpringDataTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return toDomain(repository.save(new TicketDocument(
                ticket.bookingId(),
                ticket.flightNumber(),
                ticket.seatNumber(),
                ticket.customerName(),
                ticket.status()
        )));
    }

    @Override
    public Optional<Ticket> findByBookingId(String bookingId) {
        return repository.findById(bookingId).map(this::toDomain);
    }

    private Ticket toDomain(TicketDocument document) {
        return new Ticket(
                document.getBookingId(),
                document.getFlightNumber(),
                document.getSeatNumber(),
                document.getCustomerName(),
                document.getStatus()
        );
    }
}

