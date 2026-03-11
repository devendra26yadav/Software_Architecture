package edu.miu.sa.lab9.part4.seatreservation.domain;

import java.util.Optional;

public interface SeatReservationStore {
    SeatReservation save(SeatReservation seatReservation);

    Optional<SeatReservation> findByBookingId(String bookingId);
}

