package com.airline.system.service;

import com.airline.system.model.Reservation;
import com.airline.system.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation getReservation(String id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        reservationRepository.establishReservationRelationships(
                reservation.getReservationID(),
                reservation.getPassengerID(),
                reservation.getDepartureAirportID(),
                reservation.getArrivalAirportID()
        );
    }

    public boolean updateReservation(String id, Reservation reservation) {
        if (!reservationRepository.existsById(id)) {
            return false;
        }
        reservation.setReservationID(id);
        reservationRepository.save(reservation);
        reservationRepository.establishReservationRelationships(
                id,
                reservation.getPassengerID(),
                reservation.getDepartureAirportID(),
                reservation.getArrivalAirportID()
        );
        return true;
    }

    @org.springframework.transaction.annotation.Transactional
    public boolean deleteReservation(String id) {
        if (!reservationRepository.existsById(id)) {
            return false;
        }
        reservationRepository.deleteReservationCascade(id);
        return true;
    }

    public List<Reservation> getReservationsByFlight(String flightID) {
        return reservationRepository.findReservationsByFlightID(flightID);
    }
}
