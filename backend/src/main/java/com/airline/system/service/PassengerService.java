package com.airline.system.service;

import com.airline.system.model.Baggage;
import com.airline.system.model.Passenger;
import com.airline.system.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Transactional(readOnly = true)
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Passenger getPassenger(String id) {
        return passengerRepository.findById(id).orElse(null);
    }

    public void addPassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    public boolean updatePassenger(String id, Passenger passenger) {
        if (!passengerRepository.existsById(id)) {
            return false;
        }
        passenger.setPassengerID(id);
        passengerRepository.save(passenger);
        return true;
    }

    @Transactional
    public boolean deletePassenger(String id) {
        if (!passengerRepository.existsById(id)) {
            return false;
        }
        passengerRepository.deletePassengerCascade(id);
        return true;
    }

    public List<Passenger> getPassengersByFlight(String flightID) {
        return passengerRepository.findPassengersByFlightID(flightID);
    }

    public List<Baggage> getBaggageByPassenger(String passengerID) {
        return passengerRepository.findBaggageByPassengerID(passengerID);
    }
}
