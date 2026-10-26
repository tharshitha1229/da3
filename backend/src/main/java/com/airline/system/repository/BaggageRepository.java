package com.airline.system.repository;

import com.airline.system.model.Baggage;
import com.airline.system.model.BaggageTracking;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggageRepository extends Neo4jRepository<Baggage, String> {

    /**
     * Traversal: Traces a bag's route through all recorded checkpoints in chronological scan order.
     */
    @Query("MATCH (b:Baggage {baggageID: $baggageID})-[:TRACKED_AT]->(t:BaggageTracking) " +
           "RETURN t ORDER BY t.scanTime ASC")
    List<BaggageTracking> traceBaggageRoute(String baggageID);

    /**
     * Traversal: Finds all baggage loaded onto a specific flight.
     */
    @Query("MATCH (b:Baggage)-[:LOADED_ON]->(f:Flight {flightID: $flightID}) " +
           "RETURN b ORDER BY b.baggageID ASC")
    List<Baggage> findBaggageByFlightID(String flightID);

    /**
     * Traversal: Finds all baggage owned by a passenger.
     */
    @Query("MATCH (p:Passenger {passengerID: $passengerID})-[:OWNS_BAGGAGE]->(b:Baggage) " +
           "RETURN b ORDER BY b.baggageID ASC")
    List<Baggage> findBaggageByPassengerID(String passengerID);

    /**
     * Links baggage node to Passenger (OWNS_BAGGAGE) and Flight (LOADED_ON).
     */
    @Query("MATCH (b:Baggage {baggageID: $baggageID}) " +
           "OPTIONAL MATCH (p:Passenger {passengerID: $passengerID}) " +
           "OPTIONAL MATCH (f:Flight {flightID: $flightID}) " +
           "FOREACH (_ IN CASE WHEN p IS NOT NULL THEN [1] ELSE [] END | MERGE (p)-[:OWNS_BAGGAGE]->(b)) " +
           "FOREACH (_ IN CASE WHEN f IS NOT NULL THEN [1] ELSE [] END | MERGE (b)-[:LOADED_ON]->(f)) " +
           "RETURN b")
    Baggage linkBaggageToPassengerAndFlight(String baggageID, String passengerID, String flightID);

    /**
     * Cascading delete: Detaches and deletes a baggage node along with all associated
     * tracking scan history and ticket-baggage check-in records.
     */
    @Query("MATCH (b:Baggage {baggageID: $baggageID}) " +
           "OPTIONAL MATCH (bt:BaggageTracking) WHERE bt.baggageID = b.baggageID OR (b)-[:TRACKED_AT]->(bt) " +
           "OPTIONAL MATCH (tb:TicketBaggage) WHERE tb.baggageID = b.baggageID " +
           "DETACH DELETE bt, tb " +
           "WITH DISTINCT b " +
           "DETACH DELETE b")
    void deleteBaggageCascade(String baggageID);

    /**
     * Detaches and deletes a baggage node.
     */
    @Query("MATCH (b:Baggage {baggageID: $baggageID}) DETACH DELETE b")
    void deleteBaggageNode(String baggageID);
}
