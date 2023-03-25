package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int bill = reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();

        if(amountSent < bill){
            throw new Exception("Insufficient Amount");
        }
        boolean isValid = false;
        PaymentMode paymentMode = null;

        if(mode.equals("cash") || mode.equals("card") || mode.equals("upi")){
            isValid = true;
            if(mode.equals("cash")) paymentMode = PaymentMode.CASH;
            else if(mode.equals("card")) paymentMode = PaymentMode.CARD;
            else paymentMode = PaymentMode.UPI;
        }

        if(!isValid){
            throw new Exception("Payment mode not detected");
        }
        if(paymentMode == null){
            throw new Exception("Payment mode not detected");
        }

        Payment payment = new Payment();
        payment.setPaymentCompleted(true);
        payment.setPaymentMode(paymentMode);
        payment.setReservation(reservation);
        reservation.setPayment(payment);

        reservationRepository2.save(reservation);

        return payment;
    }
}
