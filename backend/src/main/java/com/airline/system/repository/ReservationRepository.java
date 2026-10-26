package com.airline.system.repository;

import com.airline.system.model.Reservation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends Neo4jRepository<Reservation, String> {

    List<Reservation> findByPassengerID(String passengerID);

    List<Reservation> findByBookingStatus(String bookingStatus);

    /**
     * Traversal: Finds all reservations for a specific flight.
     */
    @Query("MATCH (r:Reservation)-[:FOR_FLIGHT]->(f:Flight {flightID: $flightID}) " +
           "RETURN r ORDER BY r.reservationID ASC")
    List<Reservation> findReservationsByFlightID(String flightID);

    /**
     * Connects reservation to passenger, departure airport, and arrival airport.
     */
    @Query("MATCH (r:Reservation {reservationID: $reservationID}) " +
           "OPTIONAL MATCH (p:Passenger {passengerID: $passengerID}) " +
           "OPTIONAL MATCH (dep:Airport {airportID: $departureAirportID}) " +
           "OPTIONAL MATCH (arr:Airport {airportID: $arrivalAirportID}) " +
           "FOREACH (_ IN CASE WHEN p IS NOT NULL THEN [1] ELSE [] END | MERGE (p)-[:RESERVED]->(r)) " +
           "FOREACH (_ IN CASE WHEN dep IS NOT NULL THEN [1] ELSE [] END | MERGE (r)-[:DEPARTS_FROM]->(dep)) " +
           "FOREACH (_ IN CASE WHEN arr IS NOT NULL THEN [1] ELSE [] END | MERGE (r)-[:ARRIVES_AT]->(arr)) " +
           "RETURN r")
    Reservation establishReservationRelationships(String reservationID, String passengerID,
                                                  String departureAirportID, String arrivalAirportID);

    /**
     * Cascading delete: Detaches and deletes a reservation node along with all dependent
     * tickets, payments, and ticket-baggage assignments.
     */
    @Query("MATCH (r:Reservation {reservationID: $reservationID}) " +
           "OPTIONAL MATCH (t:Ticket) WHERE t.reservationID = r.reservationID OR (r)-[:HAS_TICKET]->(t) " +
           "OPTIONAL MATCH (pm:Payment) WHERE pm.reservationID = r.reservationID OR (pm)-[:PAID_FOR]->(r) " +
           "OPTIONAL MATCH (tb:TicketBaggage) WHERE tb.reservationID = r.reservationID " +
           "DETACH DELETE pm, tb, t " +
           "WITH DISTINCT r " +
           "DETACH DELETE r")
    void deleteReservationCascade(String reservationID);

    /**
     * Detaches and deletes a reservation node.
     */
    @Query("MATCH (r:Reservation {reservationID: $reservationID}) DETACH DELETE r")
    void deleteReservationNode(String reservationID);
}
