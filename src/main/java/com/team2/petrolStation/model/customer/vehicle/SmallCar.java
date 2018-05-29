package com.team2.petrolStation.model.customer.vehicle;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Models the values assigned to a Small Car object
 *
 * @author callummarriage
 */
public class SmallCar extends AbstractVehicle {

    public SmallCar(){
        Random random = new SecureRandom();

        this.maxQueueTime = 30;
        this.shopTime = random.nextInt(24 - 12 + 1) + 12;
        this.shopPurchase = random.nextInt(10 -5 + 1) + 5;
        this.maxFuel = random.nextInt(9 - 7  + 1) + 7;
        this.currentFuel = 0;
        this.timeInQueue = 0;
        this.size = 1.0;
    }
}
