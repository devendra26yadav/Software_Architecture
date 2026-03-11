package edu.miu.sa.lab9.part3.booking.application;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BookingRequest(
        @NotBlank String bookingId,
        @NotBlank String flightNumber,
        @NotBlank String seatNumber,
        @NotBlank String customerName,
        @NotNull @DecimalMin("0.0") BigDecimal amount,
        boolean failPayment,
        boolean failTicketIssue
) {
}

