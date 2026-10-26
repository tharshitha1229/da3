package com.airline.system.repository;

import com.airline.system.model.PassengerPhone;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerPhoneRepository extends Neo4jRepository<PassengerPhone, String> {

    @Query("MATCH (pp:PassengerPhone {passengerID: $passengerID, phone: $phone}) RETURN pp")
    Optional<PassengerPhone> findByPassengerIDAndPhone(String passengerID, String phone);

    @Query("MATCH (pp:PassengerPhone {passengerID: $passengerID, phone: $phone}) DETACH DELETE pp")
    void deleteByPassengerIDAndPhone(String passengerID, String phone);

    @Query("MATCH (pp:PassengerPhone {passengerID: $passengerID}) RETURN pp ORDER BY pp.phone ASC")
    List<PassengerPhone> findByPassengerID(String passengerID);

    @Query("MATCH (p:Passenger {passengerID: $passengerID}), (pp:PassengerPhone {phone: $phone}) " +
           "MERGE (p)-[:HAS_PHONE]->(pp) RETURN pp")
    PassengerPhone linkPhoneToPassenger(String passengerID, String phone);
}
