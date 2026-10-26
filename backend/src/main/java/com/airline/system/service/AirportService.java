package com.airline.system.service;

import com.airline.system.model.Airport;
import com.airline.system.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Transactional(readOnly = true)
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Airport getAirport(String id) {
        return airportRepository.findById(id).orElse(null);
    }

    public void addAirport(Airport airport) {
        airportRepository.save(airport);
    }

    public boolean updateAirport(String id, Airport airport) {
        if (!airportRepository.existsById(id)) {
            return false;
        }
        airport.setAirportID(id);
        airportRepository.save(airport);
        return true;
    }

    public boolean deleteAirport(String id) {
        if (!airportRepository.existsById(id)) {
            return false;
        }
        airportRepository.deleteAirportNode(id);
        return true;
    }

    public List<Airport> getDirectDestinations(String airportID) {
        return airportRepository.findDirectDestinations(airportID);
    }
}
