package edu.miu.sa.lab9.part3.seatreservation.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataSeatReservationRepository extends MongoRepository<SeatReservationDocument, String> {
}

