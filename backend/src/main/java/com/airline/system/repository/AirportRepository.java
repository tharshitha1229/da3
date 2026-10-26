package com.airline.system.repository;

import com.airline.system.model.Airport;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends Neo4jRepository<Airport, String> {

    List<Airport> findByCityIgnoreCase(String city);

    List<Airport> findByCountryIgnoreCase(String country);

    /**
     * Finds all direct destination airports reachable from a given origin airport.
     * Uses graph traversal from origin Airport through departing Flights to arrival Airports.
     */
    @Query("MATCH (orig:Airport {airportID: $airportID})<-[:DEPARTS_FROM]-(:Flight)-[:ARRIVES_AT]->(dest:Airport) " +
           "RETURN DISTINCT dest ORDER BY dest.airportName ASC")
    List<Airport> findDirectDestinations(String airportID);

    /**
     * Safely detaches and deletes an airport node and its incident relationships.
     */
    @Query("MATCH (a:Airport {airportID: $airportID}) DETACH DELETE a")
    void deleteAirportNode(String airportID);
}
