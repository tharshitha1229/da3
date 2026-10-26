package com.airline.system.repository;

import com.airline.system.model.TicketBaggage;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketBaggageRepository extends Neo4jRepository<TicketBaggage, String> {

    @Query("MATCH (tb:TicketBaggage {reservationID: $reservationID, ticketNo: $ticketNo, baggageID: $baggageID}) RETURN tb")
    Optional<TicketBaggage> findByReservationIDAndTicketNoAndBaggageID(String reservationID, String ticketNo, String baggageID);

    @Query("MATCH (tb:TicketBaggage {reservationID: $reservationID, ticketNo: $ticketNo, baggageID: $baggageID}) DETACH DELETE tb")
    void deleteByReservationIDAndTicketNoAndBaggageID(String reservationID, String ticketNo, String baggageID);

    @Query("MATCH (tb:TicketBaggage {reservationID: $reservationID, ticketNo: $ticketNo}) RETURN tb")
    List<TicketBaggage> findByReservationIDAndTicketNo(String reservationID, String ticketNo);

    @Query("MATCH (tb:TicketBaggage {baggageID: $baggageID}) RETURN tb")
    List<TicketBaggage> findByBaggageID(String baggageID);
}
