package edu.miu.sa.lab9.part4.seatreservation.domain;

public record SeatReservation(
        String bookingId,
        String flightNumber,
        String seatNumber,
        String customerName,
        ReservationStatus status
) {
}

