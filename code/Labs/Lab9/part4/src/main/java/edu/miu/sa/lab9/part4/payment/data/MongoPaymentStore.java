package edu.miu.sa.lab9.part4.payment.data;

import edu.miu.sa.lab9.part4.payment.domain.Payment;
import edu.miu.sa.lab9.part4.payment.domain.PaymentStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoPaymentStore implements PaymentStore {

    private final SpringDataPaymentRepository repository;

    public MongoPaymentStore(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        return toDomain(repository.save(new PaymentDocument(
                payment.bookingId(),
                payment.amount(),
                payment.status()
        )));
    }

    @Override
    public Optional<Payment> findByBookingId(String bookingId) {
        return repository.findById(bookingId).map(this::toDomain);
    }

    private Payment toDomain(PaymentDocument document) {
        return new Payment(document.getBookingId(), document.getAmount(), document.getStatus());
    }
}

