package com.team2.petrolstation.model.customer.vehicle;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.*;

/**
 * Models the values assigned to a FamilySedan object
 *
 * @author callummarriage
 */
public class FamilySedan extends AbstractVehicle {

    public FamilySedan(Random random){
        this.shopTime = random.nextInt(30 - 12 + 1) + 12;
        this.shopPurchase = random.nextInt(16 - 8 + 1) + 8;
        this.maxFuel = random.nextInt(18) + 12;
        this.currentFuel = 0;
        this.timeInQueue = 0;
    }

    @Override
    public int getMaxQueueTime() {
        return MAX_QUEUE_TIME_FAMILY_SEDAN;
    }

    @Override
    public Double getChanceOfGoingToShop() {
        return CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP;
    }

    @Override
    public Double getSize() {
        return SIZE_OF_FAMILY_SEDAN;
    }
}
