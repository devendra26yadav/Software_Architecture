package edu.miu.sa.lab9.part3.seatreservation.application;

import edu.miu.sa.lab9.part3.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservation;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservationStore;
import org.springframework.stereotype.Service;

@Service
public class SeatReservationService {

    private final SeatReservationStore seatReservationStore;

    public SeatReservationService(SeatReservationStore seatReservationStore) {
        this.seatReservationStore = seatReservationStore;
    }

    public SeatReservation reserve(String bookingId, String flightNumber, String seatNumber, String customerName) {
        return seatReservationStore.save(new SeatReservation(
                bookingId,
                flightNumber,
                seatNumber,
                customerName,
                ReservationStatus.RESERVED
        ));
    }

    public SeatReservation cancel(String bookingId) {
        SeatReservation existing = getByBookingId(bookingId);
        return seatReservationStore.save(new SeatReservation(
                existing.bookingId(),
                existing.flightNumber(),
                existing.seatNumber(),
                existing.customerName(),
                ReservationStatus.CANCELLED
        ));
    }

    public SeatReservation getByBookingId(String bookingId) {
        return seatReservationStore.findByBookingId(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Reservation not found: " + bookingId));
    }
}

