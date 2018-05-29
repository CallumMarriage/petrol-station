package com.team2.petrolStation.model.customer.vehicle;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Models the values assigned to a Truck object.
 *
 * @author callummarriage
 */
public class Truck extends AbstractVehicle {

    public Truck(){
        Random random = new SecureRandom();

        this.maxQueueTime = 48;
        this.shopTime = random.nextInt(36 -24 +1  ) +24;
        this.maxFuel = random.nextInt(40 - 30 + 1) + 30;
        this.currentFuel = 0;
        this.timeInQueue = 0;
        this.size = 2.0;
    }
}
