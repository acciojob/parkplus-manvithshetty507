package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot newParkingLot = new ParkingLot();
        newParkingLot.setName(name);
        newParkingLot.setAddress(address);

        parkingLotRepository1.save(newParkingLot);
        return newParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Spot newSpot = new Spot();
        newSpot.setPricePerHour(pricePerHour);

        if(numberOfWheels==2){
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels == 4){
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else {
            newSpot.setSpotType(SpotType.OTHERS);
        }
        newSpot.setOccupied(false);

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        newSpot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(newSpot);

        parkingLotRepository1.save(parkingLot);

        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        //Spot spot = spotRepository1.findById(spotId).get();

        Spot spot = null;
        List<Spot> spots = parkingLot.getSpotList();
        for(Spot sport1:spots){
            if(sport1.getId() == spotId){
                spot = sport1;
            }
        }
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
