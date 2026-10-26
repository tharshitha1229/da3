package com.airline.system.repository;

import com.airline.system.model.Airline;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepository extends Neo4jRepository<Airline, String> {

    Optional<Airline> findByIataCode(String iataCode);

    @Query("MATCH (al:Airline {airlineID: $airlineID}) DETACH DELETE al")
    void deleteAirlineNode(String airlineID);
}
