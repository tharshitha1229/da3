package com.airline.system.service;

import com.airline.system.model.BaggageTracking;
import com.airline.system.repository.BaggageTrackingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaggageTrackingService {

    private final BaggageTrackingRepository baggageTrackingRepository;

    public BaggageTrackingService(
            BaggageTrackingRepository baggageTrackingRepository) {
        this.baggageTrackingRepository = baggageTrackingRepository;
    }

    @Transactional(readOnly = true)
    public List<BaggageTracking> getAllTracking() {
        return baggageTrackingRepository.findAll();
    }

    public BaggageTracking getTracking(
            String baggageID,
            String trackingID) {

        return baggageTrackingRepository.findByBaggageIDAndTrackingID(
                baggageID,
                trackingID
        ).orElse(null);
    }

    public void addTracking(BaggageTracking tracking) {
        baggageTrackingRepository.save(tracking);
        baggageTrackingRepository.linkTrackingToBaggage(tracking.getTrackingID(), tracking.getBaggageID());
    }

    public boolean updateTracking(
            String baggageID,
            String trackingID,
            BaggageTracking tracking) {

        if (baggageTrackingRepository.findByBaggageIDAndTrackingID(baggageID, trackingID).isEmpty()) {
            return false;
        }

        tracking.setBaggageID(baggageID);
        tracking.setTrackingID(trackingID);
        baggageTrackingRepository.save(tracking);
        baggageTrackingRepository.linkTrackingToBaggage(trackingID, baggageID);
        return true;
    }

    public boolean deleteTracking(
            String baggageID,
            String trackingID) {

        if (baggageTrackingRepository.findByBaggageIDAndTrackingID(baggageID, trackingID).isEmpty()) {
            return false;
        }

        baggageTrackingRepository.deleteByBaggageIDAndTrackingID(baggageID, trackingID);
        return true;
    }
}
