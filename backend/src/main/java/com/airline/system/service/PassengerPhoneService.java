package com.airline.system.service;

import com.airline.system.model.PassengerPhone;
import com.airline.system.repository.PassengerPhoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PassengerPhoneService {

    private final PassengerPhoneRepository passengerPhoneRepository;

    public PassengerPhoneService(PassengerPhoneRepository passengerPhoneRepository) {
        this.passengerPhoneRepository = passengerPhoneRepository;
    }

    @Transactional(readOnly = true)
    public List<PassengerPhone> getAllPhones() {
        return passengerPhoneRepository.findAll();
    }

    public PassengerPhone getPhone(
            String passengerID,
            String phone) {

        return passengerPhoneRepository.findByPassengerIDAndPhone(
                passengerID,
                phone
        ).orElse(null);
    }

    public void addPhone(PassengerPhone passengerPhone) {
        passengerPhoneRepository.save(passengerPhone);
        passengerPhoneRepository.linkPhoneToPassenger(passengerPhone.getPassengerID(), passengerPhone.getPhone());
    }

    public boolean updatePhone(
            String passengerID,
            String phone,
            PassengerPhone passengerPhone) {

        if (passengerPhoneRepository.findByPassengerIDAndPhone(passengerID, phone).isEmpty()) {
            return false;
        }

        passengerPhone.setPassengerID(passengerID);
        passengerPhone.setPhone(phone);
        passengerPhoneRepository.save(passengerPhone);
        passengerPhoneRepository.linkPhoneToPassenger(passengerID, phone);
        return true;
    }

    public boolean deletePhone(
            String passengerID,
            String phone) {

        if (passengerPhoneRepository.findByPassengerIDAndPhone(passengerID, phone).isEmpty()) {
            return false;
        }

        passengerPhoneRepository.deleteByPassengerIDAndPhone(passengerID, phone);
        return true;
    }
}
