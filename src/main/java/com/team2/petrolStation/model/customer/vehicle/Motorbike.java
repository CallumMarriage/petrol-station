package com.team2.petrolStation.model.customer.vehicle;

import java.util.Random;

/**
 * Models the values assigned to a Motorbike object
 *
 * @author callummarriage
 */
public class Motorbike extends AbstractVehicle {

    public Motorbike(){
        this.maxQueueTime = 0;
        this.maxFuel = 5;
        this.currentFuel = 0;
        this.timeInQueue = 0;
        this.size = 0.75;
    }



}
