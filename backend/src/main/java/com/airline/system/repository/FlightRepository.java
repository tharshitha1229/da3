package com.airline.system.repository;

import com.airline.system.model.Flight;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends Neo4jRepository<Flight, String> {

    List<Flight> findByAirlineID(String airlineID);

    /**
     * Traversal: Finds all direct flights between an origin and destination airport.
     */
    @Query("MATCH (f:Flight)-[:DEPARTS_FROM]->(orig:Airport {airportID: $originAirportID}), " +
           "(f)-[:ARRIVES_AT]->(dest:Airport {airportID: $destinationAirportID}) " +
           "RETURN f ORDER BY f.departureTime ASC")
    List<Flight> findDirectFlights(String originAirportID, String destinationAirportID);

    /**
     * Traversal: Finds connecting flights with a 1-stop layover between origin and destination.
     * Evaluates layover connectivity where arrival at the intermediate airport precedes departure of the next leg.
     */
    @Query("MATCH (orig:Airport {airportID: $originAirportID})<-[:DEPARTS_FROM]-(f1:Flight)-[:ARRIVES_AT]->(layover:Airport), " +
           "(layover)<-[:DEPARTS_FROM]-(f2:Flight)-[:ARRIVES_AT]->(dest:Airport {airportID: $destinationAirportID}) " +
           "WHERE f1.departureTime < f2.departureTime " +
           "RETURN f1, f2")
    List<Flight> findConnectingFlights(String originAirportID, String destinationAirportID);

    /**
     * Finds flights departing from a specific airport.
     */
    @Query("MATCH (f:Flight)-[:DEPARTS_FROM]->(a:Airport {airportID: $airportID}) RETURN f ORDER BY f.departureTime ASC")
    List<Flight> findFlightsByDepartureAirport(String airportID);

    /**
     * Finds flights arriving at a specific airport.
     */
    @Query("MATCH (f:Flight)-[:ARRIVES_AT]->(a:Airport {airportID: $airportID}) RETURN f ORDER BY f.departureTime ASC")
    List<Flight> findFlightsByArrivalAirport(String airportID);

    /**
     * Connects flight to its departure airport, arrival airport, and operating airline via graph edges.
     */
    @Query("MATCH (f:Flight {flightID: $flightID}) " +
           "OPTIONAL MATCH (dep:Airport {airportID: $departureAirportID}) " +
           "OPTIONAL MATCH (arr:Airport {airportID: $arrivalAirportID}) " +
           "OPTIONAL MATCH (al:Airline {airlineID: $airlineID}) " +
           "FOREACH (_ IN CASE WHEN dep IS NOT NULL THEN [1] ELSE [] END | MERGE (f)-[:DEPARTS_FROM]->(dep)) " +
           "FOREACH (_ IN CASE WHEN arr IS NOT NULL THEN [1] ELSE [] END | MERGE (f)-[:ARRIVES_AT]->(arr)) " +
           "FOREACH (_ IN CASE WHEN al IS NOT NULL THEN [1] ELSE [] END | MERGE (f)-[:OPERATED_BY]->(al)) " +
           "RETURN f")
    Flight establishFlightRelationships(String flightID, String departureAirportID, String arrivalAirportID, String airlineID);

    /**
     * Detaches and deletes a flight node.
     */
    @Query("MATCH (f:Flight {flightID: $flightID}) DETACH DELETE f")
    void deleteFlightNode(String flightID);
}
