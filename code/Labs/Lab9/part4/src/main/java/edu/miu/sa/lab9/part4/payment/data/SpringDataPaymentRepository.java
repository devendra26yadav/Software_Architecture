package edu.miu.sa.lab9.part4.payment.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataPaymentRepository extends MongoRepository<PaymentDocument, String> {
}

