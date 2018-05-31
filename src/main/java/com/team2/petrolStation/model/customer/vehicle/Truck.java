package com.team2.petrolStation.model.customer.vehicle;

import java.util.Random;

import static com.team2.petrolStation.model.constants.VehicleConstants.CHANCE_OF_TRUCK_GOING_TO_SHOP;
import static com.team2.petrolStation.model.constants.VehicleConstants.MAX_QUEUE_TIME_TRUCK;
import static com.team2.petrolStation.model.constants.VehicleConstants.SIZE_OF_TRUCK;

/**
 * Models the values assigned to a Truck object.
 *
 * @author callummarriage
 */
public class Truck extends AbstractVehicle {

    public Truck(Random random){
        this.shopTime = random.nextInt(36 -24 +1  ) +24;
        this.maxFuel = random.nextInt(40 - 30 + 1) + 30;
        this.currentFuel = 0;
        this.timeInQueue = 0;
    }

    @Override
    public int getMaxQueueTime() {
        return MAX_QUEUE_TIME_TRUCK;
    }

    @Override
    public Double getChanceOfGoingToShop() {
        return CHANCE_OF_TRUCK_GOING_TO_SHOP;
    }

    @Override
    public Double getSize() {
        return SIZE_OF_TRUCK;
    }
}
