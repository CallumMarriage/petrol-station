package com.team2.petrolStation.model.customer.vehicle;

import java.util.Random;

/**
 * Allows for any vehicle to refuel, manage their fuel size and their size.
 *
 * @author callummarriage
 */
public abstract class AbstractVehicle implements Vehicle {

    Integer maxFuel;
    Integer currentFuel;
    Integer timeInQueue;
    Integer shopPurchase;
    Integer shopTime;

    AbstractVehicle(Random random){
    }

    public Integer getShopTime() {
        return shopTime;
    }

    public Integer getShopPurchase() {
        return shopPurchase;
    }

    @Override
    public abstract Double getChanceOfGoingToShop();

    @Override
    public Boolean act(Integer value) {
        if((this.currentFuel + value ) >= this.maxFuel){
            return true;
        } else{
            this.timeInQueue += 10;
            this.currentFuel += value;
            return false;
        }
    }

    @Override
    public Integer getTimeInQueue() {
        return this.timeInQueue;
    }

    @Override
    public Integer getMaxFuel() {
        return this.maxFuel;
    }
}
