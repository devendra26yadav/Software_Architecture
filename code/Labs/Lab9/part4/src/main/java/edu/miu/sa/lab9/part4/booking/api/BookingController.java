package edu.miu.sa.lab9.part4.booking.api;

import edu.miu.sa.lab9.part4.booking.application.BookingFlowService;
import edu.miu.sa.lab9.part4.booking.application.BookingRequest;
import edu.miu.sa.lab9.part4.booking.application.BookingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingFlowService bookingFlowService;

    public BookingController(BookingFlowService bookingFlowService) {
        this.bookingFlowService = bookingFlowService;
    }

    @PostMapping
    public BookingResult book(@Valid @RequestBody BookingRequest request) {
        return bookingFlowService.startBooking(request);
    }
}

