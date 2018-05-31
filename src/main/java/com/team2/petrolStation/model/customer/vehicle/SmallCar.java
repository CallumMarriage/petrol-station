package com.team2.petrolStation.model.customer.vehicle;

import java.security.SecureRandom;
import java.util.Random;

import static com.team2.petrolStation.model.constants.VehicleConstants.CHANCE_OF_SMALL_CAR_GOING_TO_SHOP;
import static com.team2.petrolStation.model.constants.VehicleConstants.MAX_QUEUE_TIME_SMALL_CAR;
import static com.team2.petrolStation.model.constants.VehicleConstants.SIZE_OF_SMALL_CAR;

/**
 * Models the values assigned to a Small Car object
 *
 * @author callummarriage
 */
public class SmallCar extends AbstractVehicle {

    public SmallCar(Random random){
        this.shopTime = random.nextInt(24 - 12 + 1) + 12;
        this.shopPurchase = random.nextInt(10 -5 + 1) + 5;
        this.maxFuel = random.nextInt(9 - 7  + 1) + 7;
        this.currentFuel = 0;
        this.timeInQueue = 0;
    }

    @Override
    public int getMaxQueueTime() {
        return MAX_QUEUE_TIME_SMALL_CAR;
    }

    @Override
    public Double getChanceOfGoingToShop() {
        return CHANCE_OF_SMALL_CAR_GOING_TO_SHOP;
    }

    @Override
    public Double getSize() {
        return SIZE_OF_SMALL_CAR;
    }
}
