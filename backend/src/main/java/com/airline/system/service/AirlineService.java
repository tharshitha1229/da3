package com.airline.system.service;

import com.airline.system.model.Airline;
import com.airline.system.repository.AirlineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Transactional(readOnly = true)
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Airline getAirline(String id) {
        return airlineRepository.findById(id).orElse(null);
    }

    public void addAirline(Airline airline) {
        airlineRepository.save(airline);
    }

    public boolean updateAirline(String id, Airline airline) {
        if (!airlineRepository.existsById(id)) {
            return false;
        }
        airline.setAirlineID(id);
        airlineRepository.save(airline);
        return true;
    }

    public boolean deleteAirline(String id) {
        if (!airlineRepository.existsById(id)) {
            return false;
        }
        airlineRepository.deleteAirlineNode(id);
        return true;
    }
}
