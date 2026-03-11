package edu.miu.sa.lab9.part3.seatreservation.data;

import edu.miu.sa.lab9.part3.seatreservation.domain.ReservationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("part3_seat_reservations")
public class SeatReservationDocument {

    @Id
    private String bookingId;
    private String flightNumber;
    private String seatNumber;
    private String customerName;
    private ReservationStatus status;

    public SeatReservationDocument() {
    }

    public SeatReservationDocument(String bookingId, String flightNumber, String seatNumber, String customerName, ReservationStatus status) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}

