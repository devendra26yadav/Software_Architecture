package edu.miu.sa.lab9.part3;

import edu.miu.sa.lab9.part3.booking.application.BookingRequest;
import edu.miu.sa.lab9.part3.booking.application.BookingResult;
import edu.miu.sa.lab9.part3.booking.application.BookingSagaOrchestrator;
import edu.miu.sa.lab9.part3.booking.application.BookingStatus;
import edu.miu.sa.lab9.part3.payment.application.PaymentService;
import edu.miu.sa.lab9.part3.payment.domain.Payment;
import edu.miu.sa.lab9.part3.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part3.payment.domain.PaymentStore;
import edu.miu.sa.lab9.part3.seatreservation.application.SeatReservationService;
import edu.miu.sa.lab9.part3.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservation;
import edu.miu.sa.lab9.part3.seatreservation.domain.SeatReservationStore;
import edu.miu.sa.lab9.part3.ticket.application.TicketService;
import edu.miu.sa.lab9.part3.ticket.domain.Ticket;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStatus;
import edu.miu.sa.lab9.part3.ticket.domain.TicketStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrchestrationSagaRequirementTest {

    @Test
    void successfulBookingReservesSeatProcessesPaymentAndIssuesTicket() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.orchestrator.book(new BookingRequest(
                "B-100",
                "FL-900",
                "12A",
                "Alice",
                new BigDecimal("400.00"),
                false,
                false
        ));

        assertEquals(BookingStatus.COMPLETED, result.bookingStatus());
        assertEquals(ReservationStatus.RESERVED, scenario.seatReservationService.getByBookingId("B-100").status());
        assertEquals(PaymentStatus.COMPLETED, scenario.paymentService.getByBookingId("B-100").status());
        assertEquals(TicketStatus.ISSUED, scenario.ticketService.getByBookingId("B-100").status());
    }

    @Test
    void paymentFailureCancelsSeatReservationAndDoesNotIssueTicket() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.orchestrator.book(new BookingRequest(
                "B-101",
                "FL-901",
                "14C",
                "Bob",
                new BigDecimal("510.00"),
                true,
                false
        ));

        assertEquals(BookingStatus.PAYMENT_FAILED, result.bookingStatus());
        assertEquals(ReservationStatus.CANCELLED, scenario.seatReservationService.getByBookingId("B-101").status());
        assertEquals(PaymentStatus.FAILED, scenario.paymentService.getByBookingId("B-101").status());
        assertThrows(IllegalArgumentException.class, () -> scenario.ticketService.getByBookingId("B-101"));
    }

    @Test
    void ticketFailureRefundsPaymentAndCancelsSeatReservation() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.orchestrator.book(new BookingRequest(
                "B-102",
                "FL-902",
                "18D",
                "Charlie",
                new BigDecimal("620.00"),
                false,
                true
        ));

        assertEquals(BookingStatus.TICKET_FAILED, result.bookingStatus());
        assertEquals(ReservationStatus.CANCELLED, scenario.seatReservationService.getByBookingId("B-102").status());
        assertEquals(PaymentStatus.REFUNDED, scenario.paymentService.getByBookingId("B-102").status());
        assertEquals(TicketStatus.FAILED, scenario.ticketService.getByBookingId("B-102").status());
    }

    private static final class Scenario {
        private final SeatReservationService seatReservationService;
        private final PaymentService paymentService;
        private final TicketService ticketService;
        private final BookingSagaOrchestrator orchestrator;

        private Scenario() {
            this.seatReservationService = new SeatReservationService(new InMemorySeatReservationStore());
            this.paymentService = new PaymentService(new InMemoryPaymentStore());
            this.ticketService = new TicketService(new InMemoryTicketStore());
            this.orchestrator = new BookingSagaOrchestrator(seatReservationService, paymentService, ticketService);
        }
    }

    private static final class InMemorySeatReservationStore implements SeatReservationStore {
        private final Map<String, SeatReservation> store = new LinkedHashMap<>();

        @Override
        public SeatReservation save(SeatReservation seatReservation) {
            store.put(seatReservation.bookingId(), seatReservation);
            return seatReservation;
        }

        @Override
        public Optional<SeatReservation> findByBookingId(String bookingId) {
            return Optional.ofNullable(store.get(bookingId));
        }
    }

    private static final class InMemoryPaymentStore implements PaymentStore {
        private final Map<String, Payment> store = new LinkedHashMap<>();

        @Override
        public Payment save(Payment payment) {
            store.put(payment.bookingId(), payment);
            return payment;
        }

        @Override
        public Optional<Payment> findByBookingId(String bookingId) {
            return Optional.ofNullable(store.get(bookingId));
        }
    }

    private static final class InMemoryTicketStore implements TicketStore {
        private final Map<String, Ticket> store = new LinkedHashMap<>();

        @Override
        public Ticket save(Ticket ticket) {
            store.put(ticket.bookingId(), ticket);
            return ticket;
        }

        @Override
        public Optional<Ticket> findByBookingId(String bookingId) {
            return Optional.ofNullable(store.get(bookingId));
        }
    }
}
