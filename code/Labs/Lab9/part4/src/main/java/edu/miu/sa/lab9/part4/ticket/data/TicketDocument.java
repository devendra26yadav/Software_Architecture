package edu.miu.sa.lab9.part4.ticket.data;

import edu.miu.sa.lab9.part4.ticket.domain.TicketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("part4_tickets")
public class TicketDocument {

    @Id
    private String bookingId;
    private String flightNumber;
    private String seatNumber;
    private String customerName;
    private TicketStatus status;

    public TicketDocument() {
    }

    public TicketDocument(String bookingId, String flightNumber, String seatNumber, String customerName, TicketStatus status) {
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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}

