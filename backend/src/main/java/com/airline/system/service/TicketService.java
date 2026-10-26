package com.airline.system.service;

import com.airline.system.model.Ticket;
import com.airline.system.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(String reservationID, String ticketNo) {
        return ticketRepository.findByReservationIDAndTicketNo(reservationID, ticketNo).orElse(null);
    }

    public void addTicket(Ticket ticket) {
        ticketRepository.save(ticket);
        ticketRepository.linkTicketToReservation(ticket.getTicketNo(), ticket.getReservationID());
    }

    public boolean updateTicket(String reservationID, String ticketNo, Ticket ticket) {
        if (ticketRepository.findByReservationIDAndTicketNo(reservationID, ticketNo).isEmpty()) {
            return false;
        }
        ticket.setReservationID(reservationID);
        ticket.setTicketNo(ticketNo);
        ticketRepository.save(ticket);
        ticketRepository.linkTicketToReservation(ticketNo, reservationID);
        return true;
    }

    @org.springframework.transaction.annotation.Transactional
    public boolean deleteTicket(String reservationID, String ticketNo) {
        if (ticketRepository.findByReservationIDAndTicketNo(reservationID, ticketNo).isEmpty()) {
            return false;
        }
        ticketRepository.deleteTicketCascade(reservationID, ticketNo);
        return true;
    }
}
