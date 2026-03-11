package edu.miu.sa.lab9.part3.booking.api;

import edu.miu.sa.lab9.part3.booking.application.BookingRequest;
import edu.miu.sa.lab9.part3.booking.application.BookingResult;
import edu.miu.sa.lab9.part3.booking.application.BookingSagaOrchestrator;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingSagaOrchestrator bookingSagaOrchestrator;

    public BookingController(BookingSagaOrchestrator bookingSagaOrchestrator) {
        this.bookingSagaOrchestrator = bookingSagaOrchestrator;
    }

    @PostMapping
    public BookingResult book(@Valid @RequestBody BookingRequest request) {
        return bookingSagaOrchestrator.book(request);
    }
}

