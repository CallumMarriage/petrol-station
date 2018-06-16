package com.team2.petrolstation.model.customer.vehicle;

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
    private Boolean hasRefueled;

    AbstractVehicle(){
        hasRefueled = false;
    }

    public Integer getShopTime() {
        return shopTime;
    }

    public Integer getShopPurchase() {
        return shopPurchase;
    }

    /**
     * Refuels this vehicle, if the vehicle has finished refueling it returns true if not it adds to the current fuel level and returns false
     * @param value the amount that the fuel will increase by
     * @return boolean
     */
    @Override
    public Boolean act(Integer value) {
        if((this.currentFuel + value ) >= this.maxFuel){
            this.currentFuel = maxFuel;
            if(hasRefueled){
                return false;
            } else {
                hasRefueled = true;
                return true;
            }
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

    @Override
    public Boolean decide(Random random) {
        return (getTimeInQueue() < getMaxQueueTime() && random.nextDouble() < getChanceOfGoingToShop());
    }
}
