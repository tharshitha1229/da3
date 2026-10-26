package com.airline.system.repository;

import com.airline.system.model.Baggage;
import com.airline.system.model.Passenger;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends Neo4jRepository<Passenger, String> {

    Optional<Passenger> findByEmail(String email);

    List<Passenger> findByNameContainingIgnoreCase(String name);

    /**
     * Traversal: Finds all passengers booked on a flight by traversing through Reservations.
     * Replaces a 3-table SQL JOIN: PASSENGER -> RESERVATION -> FLIGHT.
     */
    @Query("MATCH (p:Passenger)-[:RESERVED]->(r:Reservation)-[:FOR_FLIGHT]->(f:Flight {flightID: $flightID}) " +
           "RETURN DISTINCT p ORDER BY p.name ASC")
    List<Passenger> findPassengersByFlightID(String flightID);

    /**
     * Traversal: Retrieves all baggage checked by a passenger.
     */
    @Query("MATCH (p:Passenger {passengerID: $passengerID})-[:OWNS_BAGGAGE]->(b:Baggage) " +
           "RETURN b ORDER BY b.baggageID ASC")
    List<Baggage> findBaggageByPassengerID(String passengerID);

    /**
     * Cascading delete: Detaches and deletes a passenger node along with all dependent
     * passenger phones, reservations, tickets, payments, baggage, tracking scans,
     * and ticket-baggage check-in records.
     */
    @Query("MATCH (p:Passenger {passengerID: $passengerID}) " +
           "OPTIONAL MATCH (ph:PassengerPhone) WHERE ph.passengerID = p.passengerID OR (p)-[:HAS_PHONE]->(ph) " +
           "DETACH DELETE ph " +
           "WITH DISTINCT p " +
           "OPTIONAL MATCH (r_pre:Reservation) WHERE r_pre.passengerID = p.passengerID OR (p)-[:RESERVED]->(r_pre) " +
           "OPTIONAL MATCH (tb_pre:TicketBaggage) WHERE tb_pre.reservationID = r_pre.reservationID " +
           "OPTIONAL MATCH (b:Baggage) WHERE (p)-[:OWNS_BAGGAGE]->(b) OR (b)-[:CHECKED_FOR]->(r_pre) OR b.baggageID = tb_pre.baggageID " +
           "OPTIONAL MATCH (bt:BaggageTracking) WHERE bt.baggageID = b.baggageID OR (b)-[:TRACKED_AT]->(bt) " +
           "OPTIONAL MATCH (tb_bag:TicketBaggage) WHERE tb_bag.baggageID = b.baggageID " +
           "DETACH DELETE bt, tb_bag, b " +
           "WITH DISTINCT p " +
           "OPTIONAL MATCH (r:Reservation) WHERE r.passengerID = p.passengerID OR (p)-[:RESERVED]->(r) " +
           "OPTIONAL MATCH (t:Ticket) WHERE t.reservationID = r.reservationID OR (r)-[:HAS_TICKET]->(t) " +
           "OPTIONAL MATCH (pm:Payment) WHERE pm.reservationID = r.reservationID OR (pm)-[:PAID_FOR]->(r) " +
           "OPTIONAL MATCH (tb_res:TicketBaggage) WHERE tb_res.reservationID = r.reservationID " +
           "DETACH DELETE pm, tb_res, t, r " +
           "WITH DISTINCT p " +
           "DETACH DELETE p")
    void deletePassengerCascade(String passengerID);

    /**
     * Detaches and deletes a passenger node and its outgoing relationships.
     */
    @Query("MATCH (p:Passenger {passengerID: $passengerID}) DETACH DELETE p")
    void deletePassengerNode(String passengerID);
}
