package com.airline.system.repository;

import com.airline.system.model.BaggageTracking;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaggageTrackingRepository extends Neo4jRepository<BaggageTracking, String> {

    @Query("MATCH (t:BaggageTracking {trackingID: $trackingID, baggageID: $baggageID}) RETURN t")
    Optional<BaggageTracking> findByBaggageIDAndTrackingID(String baggageID, String trackingID);

    @Query("MATCH (t:BaggageTracking {trackingID: $trackingID, baggageID: $baggageID}) DETACH DELETE t")
    void deleteByBaggageIDAndTrackingID(String baggageID, String trackingID);

    @Query("MATCH (t:BaggageTracking {baggageID: $baggageID}) RETURN t ORDER BY t.scanTime ASC")
    List<BaggageTracking> findByBaggageID(String baggageID);

    @Query("MATCH (b:Baggage {baggageID: $baggageID}), (t:BaggageTracking {trackingID: $trackingID}) " +
           "MERGE (b)-[:TRACKED_AT]->(t) RETURN t")
    BaggageTracking linkTrackingToBaggage(String trackingID, String baggageID);
}
