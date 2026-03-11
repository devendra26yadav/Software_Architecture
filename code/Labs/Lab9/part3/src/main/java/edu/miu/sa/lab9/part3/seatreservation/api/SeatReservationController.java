package edu.miu.sa.lab9.part3.seatreservation.api;

import edu.miu.sa.lab9.part3.seatreservation.application.SeatReservationService;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seat-reservations")
public class SeatReservationController {

    private final SeatReservationService seatReservationService;

    public SeatReservationController(SeatReservationService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }

    @GetMapping("/{bookingId}")
    public SeatReservation findByBookingId(@PathVariable String bookingId) {
        return seatReservationService.getByBookingId(bookingId);
    }
}

