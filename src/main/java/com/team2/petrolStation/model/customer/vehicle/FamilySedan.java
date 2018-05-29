package com.team2.petrolStation.model.customer.vehicle;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Models the values assigned to a FamilySedan object
 *
 * @author callummarriage
 */
public class FamilySedan extends AbstractVehicle {

    public FamilySedan(){
        Random random = new SecureRandom();

        this.maxQueueTime = 60;
        this.shopTime = random.nextInt(30 - 12 + 1) + 12;
        this.shopPurchase = random.nextInt(16 - 8 + 1) + 8;
        this.maxFuel = random.nextInt(18) + 12;
        this.currentFuel = 0;
        this.timeInQueue = 0;
        this.size = 1.5;
    }
}
