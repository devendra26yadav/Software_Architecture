package edu.miu.sa.lab9.part3.payment.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataPaymentRepository extends MongoRepository<PaymentDocument, String> {
}

