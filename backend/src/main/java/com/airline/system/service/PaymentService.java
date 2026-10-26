package com.airline.system.service;

import com.airline.system.model.Payment;
import com.airline.system.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Payment getPayment(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
        paymentRepository.linkPaymentToReservation(payment.getPaymentID(), payment.getReservationID());
    }

    public boolean updatePayment(String id, Payment payment) {
        if (!paymentRepository.existsById(id)) {
            return false;
        }
        payment.setPaymentID(id);
        paymentRepository.save(payment);
        paymentRepository.linkPaymentToReservation(id, payment.getReservationID());
        return true;
    }

    public boolean deletePayment(String id) {
        if (!paymentRepository.existsById(id)) {
            return false;
        }
        paymentRepository.deletePaymentNode(id);
        return true;
    }
}
