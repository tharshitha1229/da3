package com.airline.system.repository;

import com.airline.system.model.Payment;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends Neo4jRepository<Payment, String> {

    @Query("MATCH (p:Payment {reservationID: $reservationID}) RETURN p ORDER BY p.paymentID ASC")
    List<Payment> findByReservationID(String reservationID);

    @Query("MATCH (p:Payment {ticketNo: $ticketNo}) RETURN p ORDER BY p.paymentID ASC")
    List<Payment> findByTicketNo(String ticketNo);

    @Query("MATCH (p:Payment {paymentID: $paymentID}), (r:Reservation {reservationID: $reservationID}) " +
           "MERGE (p)-[:PAID_FOR]->(r) RETURN p")
    Payment linkPaymentToReservation(String paymentID, String reservationID);

    @Query("MATCH (p:Payment {paymentID: $paymentID}) DETACH DELETE p")
    void deletePaymentNode(String paymentID);
}
