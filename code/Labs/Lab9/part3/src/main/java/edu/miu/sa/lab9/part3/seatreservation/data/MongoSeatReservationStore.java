package edu.miu.sa.lab9.part3.seatreservation.data;

import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservation;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservationStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoSeatReservationStore implements SeatReservationStore {

    private final SpringDataSeatReservationRepository repository;

    public MongoSeatReservationStore(SpringDataSeatReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public SeatReservation save(SeatReservation seatReservation) {
        return toDomain(repository.save(new SeatReservationDocument(
                seatReservation.bookingId(),
                seatReservation.flightNumber(),
                seatReservation.seatNumber(),
                seatReservation.customerName(),
                seatReservation.status()
        )));
    }

    @Override
    public Optional<SeatReservation> findByBookingId(String bookingId) {
        return repository.findById(bookingId).map(this::toDomain);
    }

    private SeatReservation toDomain(SeatReservationDocument document) {
        return new SeatReservation(
                document.getBookingId(),
                document.getFlightNumber(),
                document.getSeatNumber(),
                document.getCustomerName(),
                document.getStatus()
        );
    }
}

