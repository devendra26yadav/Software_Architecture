package edu.miu.sa.lab9.part4;

import edu.miu.sa.lab9.part4.booking.application.BookingFlowService;
import edu.miu.sa.lab9.part4.booking.application.BookingRequest;
import edu.miu.sa.lab9.part4.booking.application.BookingResult;
import edu.miu.sa.lab9.part4.booking.application.BookingStatus;
import edu.miu.sa.lab9.part4.booking.events.BookingRequestedEvent;
import edu.miu.sa.lab9.part4.booking.events.PaymentCompletedEvent;
import edu.miu.sa.lab9.part4.booking.events.PaymentFailedEvent;
import edu.miu.sa.lab9.part4.booking.events.SeatReservedEvent;
import edu.miu.sa.lab9.part4.booking.events.TicketIssueFailedEvent;
import edu.miu.sa.lab9.part4.payment.application.PaymentService;
import edu.miu.sa.lab9.part4.payment.domain.Payment;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStatus;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStore;
import edu.miu.sa.lab9.part4.seatreservation.application.SeatReservationService;
import edu.miu.sa.lab9.part4.seatreservation.domain.ReservationStatus;
import edu.miu.sa.lab9.part4.seatreservation.domain.SeatReservation;
import edu.miu.sa.lab9.part4.seatreservation.domain.SeatReservationStore;
import edu.miu.sa.lab9.part4.shared.DomainEventPublisher;
import edu.miu.sa.lab9.part4.ticket.application.TicketService;
import edu.miu.sa.lab9.part4.ticket.domain.Ticket;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStatus;
import edu.miu.sa.lab9.part4.ticket.domain.TicketStore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChoreographySagaRequirementTest {

    @Test
    void successfulBookingFlowsThroughAllThreeServices() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.bookingFlowService.startBooking(new BookingRequest(
                "C-100",
                "FL-930",
                "21A",
                "Diana",
                new BigDecimal("455.00"),
                false,
                false
        ));

        assertEquals(BookingStatus.COMPLETED, result.bookingStatus());
        assertEquals(ReservationStatus.RESERVED, scenario.seatReservationService.getByBookingId("C-100").status());
        assertEquals(PaymentStatus.COMPLETED, scenario.paymentService.getByBookingId("C-100").status());
        assertEquals(TicketStatus.ISSUED, scenario.ticketService.getByBookingId("C-100").status());
    }

    @Test
    void paymentFailureCancelsSeatReservationThroughEvents() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.bookingFlowService.startBooking(new BookingRequest(
                "C-101",
                "FL-931",
                "22B",
                "Ethan",
                new BigDecimal("530.00"),
                true,
                false
        ));

        assertEquals(BookingStatus.PAYMENT_FAILED, result.bookingStatus());
        assertEquals(ReservationStatus.CANCELLED, scenario.seatReservationService.getByBookingId("C-101").status());
        assertEquals(PaymentStatus.FAILED, scenario.paymentService.getByBookingId("C-101").status());
        assertThrows(IllegalArgumentException.class, () -> scenario.ticketService.getByBookingId("C-101"));
    }

    @Test
    void ticketFailureRefundsPaymentAndCancelsSeatReservationThroughEvents() {
        Scenario scenario = new Scenario();

        BookingResult result = scenario.bookingFlowService.startBooking(new BookingRequest(
                "C-102",
                "FL-932",
                "23C",
                "Fatima",
                new BigDecimal("615.00"),
                false,
                true
        ));

        assertEquals(BookingStatus.TICKET_FAILED, result.bookingStatus());
        assertEquals(ReservationStatus.CANCELLED, scenario.seatReservationService.getByBookingId("C-102").status());
        assertEquals(PaymentStatus.REFUNDED, scenario.paymentService.getByBookingId("C-102").status());
        assertEquals(TicketStatus.FAILED, scenario.ticketService.getByBookingId("C-102").status());
    }

    private static final class Scenario {
        private final TestEventBus eventBus = new TestEventBus();
        private final SeatReservationService seatReservationService;
        private final PaymentService paymentService;
        private final TicketService ticketService;
        private final BookingFlowService bookingFlowService;

        private Scenario() {
            this.seatReservationService = new SeatReservationService(new InMemorySeatReservationStore(), eventBus);
            this.paymentService = new PaymentService(new InMemoryPaymentStore(), eventBus);
            this.ticketService = new TicketService(new InMemoryTicketStore(), eventBus);
            this.bookingFlowService = new BookingFlowService(eventBus, seatReservationService, paymentService, ticketService);
            this.eventBus.register(seatReservationService, paymentService, ticketService);
        }
    }

    private static final class TestEventBus implements DomainEventPublisher {
        private SeatReservationService seatReservationService;
        private PaymentService paymentService;
        private TicketService ticketService;

        void register(
                SeatReservationService seatReservationService,
                PaymentService paymentService,
                TicketService ticketService
        ) {
            this.seatReservationService = seatReservationService;
            this.paymentService = paymentService;
            this.ticketService = ticketService;
        }

        @Override
        public void publish(Object event) {
            if (event instanceof BookingRequestedEvent bookingRequestedEvent) {
                seatReservationService.on(bookingRequestedEvent);
                return;
            }
            if (event instanceof SeatReservedEvent seatReservedEvent) {
                paymentService.on(seatReservedEvent);
                return;
            }
            if (event instanceof PaymentCompletedEvent paymentCompletedEvent) {
                ticketService.on(paymentCompletedEvent);
                return;
            }
            if (event instanceof PaymentFailedEvent paymentFailedEvent) {
                seatReservationService.on(paymentFailedEvent);
                return;
            }
            if (event instanceof TicketIssueFailedEvent ticketIssueFailedEvent) {
                paymentService.on(ticketIssueFailedEvent);
                seatReservationService.on(ticketIssueFailedEvent);
            }
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
