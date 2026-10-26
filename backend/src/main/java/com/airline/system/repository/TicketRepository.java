package com.airline.system.repository;

import com.airline.system.model.Ticket;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends Neo4jRepository<Ticket, String> {

    @Query("MATCH (t:Ticket {ticketNo: $ticketNo, reservationID: $reservationID}) RETURN t")
    Optional<Ticket> findByReservationIDAndTicketNo(String reservationID, String ticketNo);

    /**
     * Cascading delete: Detaches and deletes a ticket node along with all associated
     * payments and ticket-baggage check-in records.
     */
    @Query("MATCH (t:Ticket {ticketNo: $ticketNo, reservationID: $reservationID}) " +
           "OPTIONAL MATCH (pm:Payment) WHERE (pm.ticketNo = t.ticketNo AND pm.reservationID = t.reservationID) OR pm.ticketNo = t.ticketNo " +
           "OPTIONAL MATCH (tb:TicketBaggage) WHERE (tb.ticketNo = t.ticketNo AND tb.reservationID = t.reservationID) OR tb.ticketNo = t.ticketNo " +
           "DETACH DELETE pm, tb " +
           "WITH DISTINCT t " +
           "DETACH DELETE t")
    void deleteTicketCascade(String reservationID, String ticketNo);

    @Query("MATCH (t:Ticket {ticketNo: $ticketNo, reservationID: $reservationID}) DETACH DELETE t")
    void deleteByReservationIDAndTicketNo(String reservationID, String ticketNo);

    @Query("MATCH (t:Ticket {reservationID: $reservationID}) RETURN t ORDER BY t.ticketNo ASC")
    List<Ticket> findByReservationID(String reservationID);

    @Query("MATCH (t:Ticket {ticketNo: $ticketNo}), (r:Reservation {reservationID: $reservationID}) " +
           "MERGE (r)-[:HAS_TICKET]->(t) RETURN t")
    Ticket linkTicketToReservation(String ticketNo, String reservationID);
}
