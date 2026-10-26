package com.airline.system.service;

import com.airline.system.model.TicketBaggage;
import com.airline.system.repository.TicketBaggageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TicketBaggageService {

    private final TicketBaggageRepository ticketBaggageRepository;

    public TicketBaggageService(TicketBaggageRepository ticketBaggageRepository) {
        this.ticketBaggageRepository = ticketBaggageRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketBaggage> getAllTicketBaggage() {
        return ticketBaggageRepository.findAll();
    }

    public TicketBaggage getTicketBaggage(
            String reservationID,
            String ticketNo,
            String baggageID) {

        return ticketBaggageRepository.findByReservationIDAndTicketNoAndBaggageID(
                reservationID,
                ticketNo,
                baggageID
        ).orElse(null);
    }

    public void addTicketBaggage(TicketBaggage ticketBaggage) {
        ticketBaggageRepository.save(ticketBaggage);
    }

    public boolean updateTicketBaggage(
            String reservationID,
            String ticketNo,
            String baggageID,
            TicketBaggage ticketBaggage) {

        if (ticketBaggageRepository.findByReservationIDAndTicketNoAndBaggageID(reservationID, ticketNo, baggageID).isEmpty()) {
            return false;
        }

        ticketBaggage.setReservationID(reservationID);
        ticketBaggage.setTicketNo(ticketNo);
        ticketBaggage.setBaggageID(baggageID);
        ticketBaggageRepository.save(ticketBaggage);
        return true;
    }

    public boolean deleteTicketBaggage(
            String reservationID,
            String ticketNo,
            String baggageID) {

        if (ticketBaggageRepository.findByReservationIDAndTicketNoAndBaggageID(reservationID, ticketNo, baggageID).isEmpty()) {
            return false;
        }

        ticketBaggageRepository.deleteByReservationIDAndTicketNoAndBaggageID(
                reservationID,
                ticketNo,
                baggageID
        );
        return true;
    }
}
