package com.airline.system.service;

import com.airline.system.model.Flight;
import com.airline.system.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Flight getFlight(String id) {
        return flightRepository.findById(id).orElse(null);
    }

    public void addFlight(Flight flight) {
        flightRepository.save(flight);
        String depId = flight.getDepartureAirport() != null ? flight.getDepartureAirport().getAirportID() : null;
        String arrId = flight.getArrivalAirport() != null ? flight.getArrivalAirport().getAirportID() : null;
        flightRepository.establishFlightRelationships(flight.getFlightID(), depId, arrId, flight.getAirlineID());
    }

    public boolean updateFlight(String id, Flight flight) {
        if (!flightRepository.existsById(id)) {
            return false;
        }
        flight.setFlightID(id);
        flightRepository.save(flight);
        String depId = flight.getDepartureAirport() != null ? flight.getDepartureAirport().getAirportID() : null;
        String arrId = flight.getArrivalAirport() != null ? flight.getArrivalAirport().getAirportID() : null;
        flightRepository.establishFlightRelationships(id, depId, arrId, flight.getAirlineID());
        return true;
    }

    public boolean deleteFlight(String id) {
        if (!flightRepository.existsById(id)) {
            return false;
        }
        flightRepository.deleteFlightNode(id);
        return true;
    }

    public List<Flight> getDirectFlights(String origin, String destination) {
        return flightRepository.findDirectFlights(origin, destination);
    }

    public List<Flight> getConnectingFlights(String origin, String destination) {
        return flightRepository.findConnectingFlights(origin, destination);
    }
}
