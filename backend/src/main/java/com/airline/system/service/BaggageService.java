package com.airline.system.service;

import com.airline.system.model.Baggage;
import com.airline.system.model.BaggageTracking;
import com.airline.system.repository.BaggageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaggageService {

    private final BaggageRepository baggageRepository;

    public BaggageService(BaggageRepository baggageRepository) {
        this.baggageRepository = baggageRepository;
    }

    @Transactional(readOnly = true)
    public List<Baggage> getAllBaggage() {
        return baggageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Baggage getBaggage(String id) {
        return baggageRepository.findById(id).orElse(null);
    }

    public void addBaggage(Baggage baggage) {
        baggageRepository.save(baggage);
    }

    public boolean updateBaggage(String id, Baggage baggage) {
        if (!baggageRepository.existsById(id)) {
            return false;
        }
        baggage.setBaggageID(id);
        baggageRepository.save(baggage);
        return true;
    }

    @org.springframework.transaction.annotation.Transactional
    public boolean deleteBaggage(String id) {
        if (!baggageRepository.existsById(id)) {
            return false;
        }
        baggageRepository.deleteBaggageCascade(id);
        return true;
    }

    public List<BaggageTracking> traceBaggageRoute(String baggageID) {
        return baggageRepository.traceBaggageRoute(baggageID);
    }
}
