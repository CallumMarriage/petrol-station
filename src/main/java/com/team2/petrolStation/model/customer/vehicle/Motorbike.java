package com.team2.petrolStation.model.customer.vehicle;

import static com.team2.petrolStation.model.constants.VehicleConstants.SIZE_OF_MOTORBIKE;

/**
 * Models the values assigned to a Motorbike object
 *
 * @author callummarriage
 */
public class Motorbike extends AbstractVehicle {

    public Motorbike(){
        this.maxFuel = 5;
        this.currentFuel = 0;
        this.timeInQueue = 0;
    }

    @Override
    public int getMaxQueueTime() {
        return 0;
    }

    @Override
    public Double getChanceOfGoingToShop() {
        return 0.0;
    }

    @Override
    public Double getSize() {
        return SIZE_OF_MOTORBIKE;
    }
}
