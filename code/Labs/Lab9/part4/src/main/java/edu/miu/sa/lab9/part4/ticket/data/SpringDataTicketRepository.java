package edu.miu.sa.lab9.part4.ticket.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataTicketRepository extends MongoRepository<TicketDocument, String> {
}

