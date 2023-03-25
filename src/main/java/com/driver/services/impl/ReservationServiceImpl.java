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

        User user;
        try{
            user = userRepository3.findById(userId).get();
        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot;
        try{
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }

        try{
            List<Spot> spots = parkingLot.getSpotList();
            int minTime = Integer.MAX_VALUE;
            int revSpot = -1;
            int finalWheels = 0;

            for(Spot spot:spots) {
                int wheels;
                if (spot.getSpotType() == SpotType.TWO_WHEELER)
                    wheels = 2;
                else if (spot.getSpotType() == SpotType.FOUR_WHEELER)
                    wheels = 4;
                else
                    wheels = 6;

                if (wheels >= numberOfWheels) {
                    if (timeInHours * spot.getPricePerHour() < minTime) {
                        minTime = Math.min(minTime, timeInHours * spot.getPricePerHour());
                        revSpot = spot.getId();
                        finalWheels = wheels;
                    }
                }
            }
            if(revSpot == -1){
                throw new Exception("Cannot make reservation");
            }


            Spot curSpot = spotRepository3.findById(revSpot).get();

            if(finalWheels == 0){
                throw new Exception("Cannot make reservation");
            }
            else if(finalWheels == 2){
                curSpot.setSpotType(SpotType.TWO_WHEELER);
            }
            else if(finalWheels == 4){
                curSpot.setSpotType(SpotType.FOUR_WHEELER);
            }
            else curSpot.setSpotType(SpotType.OTHERS);

            curSpot.setOccupied(true);


            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setUser(user);
            reservation.setSpot(curSpot);
            user.getReservationList().add(reservation);
            curSpot.getReservationList().add(reservation);

            return reservation;

        }catch(Exception e){
            return null;
        }
    }

}
