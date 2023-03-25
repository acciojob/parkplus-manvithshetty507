package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        User user = null;
        try{
            user = userRepository3.findById(userId).get();
        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = null;
        try{
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }

        if(user == null || parkingLot == null) return null;


        try{
            List<Spot> spots = parkingLot.getSpotList();

            int minTime = Integer.MAX_VALUE;
            int finalSpot = -1;
            int finalWheels = 0;

            for(Spot spot:spots) {
                int wheels = 0;
                if (spot.getSpotType().equals(SpotType.TWO_WHEELER))
                    wheels = 2;
                else if (spot.getSpotType().equals(SpotType.FOUR_WHEELER))
                    wheels = 4;
                else
                    wheels = 6;

                if (wheels >= numberOfWheels && !spot.getOccupied()) {
                    if (timeInHours * spot.getPricePerHour() < minTime) {
                        minTime = Math.min(minTime, timeInHours * spot.getPricePerHour());
                        finalSpot = spot.getId();
                        finalWheels = wheels;
                    }
                }
            }


            if(finalSpot == -1){
                throw new Exception("Cannot make reservation");
            }

            Spot curSpot = spotRepository3.findById(finalSpot).get();

            if(finalWheels == 2){
                curSpot.setSpotType(SpotType.TWO_WHEELER);
            }
            else if(finalWheels == 4){
                curSpot.setSpotType(SpotType.FOUR_WHEELER);
            }
            else curSpot.setSpotType(SpotType.OTHERS);

            curSpot.setOccupied(true);


            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(curSpot);
            reservation.setUser(user);

            user.getReservationList().add(reservation);
            curSpot.getReservationList().add(reservation);

            userRepository3.save(user);
            return reservation;

        }catch(Exception e){
            return null;
        }


    }

}
