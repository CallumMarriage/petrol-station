package com.team2.petrolStation.model.customer.vehicle;

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

    public Integer getShopTime() {
        return shopTime;
    }

    public Integer getShopPurchase() {
        return shopPurchase;
    }

    @Override
    public abstract Double getChanceOfGoingToShop();

    /**
     * Refuels this vehicle, if the vehicle has finished refueling it returns true if not it adds to the current fuel level and returns false
     * @param value the amount that the fuel will increase by
     * @return boolean
     */
    @Override
    public Boolean act(Integer value) {
        if((this.currentFuel + value ) >= (this.maxFuel + 10)){
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
