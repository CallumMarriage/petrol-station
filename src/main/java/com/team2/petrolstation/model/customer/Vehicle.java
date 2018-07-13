package com.team2.petrolstation.model.customer;

import java.util.Random;

/**
 * Allows for any vehicle to refuel, manage their fuel size and their size.
 * We chose this implementation as all of the vehicles behave in the same way, it is simpler to have a single class that has a number of fields than a class for each vehicle type
 * The truck field is used so that we know that a specific instance is a Truck for use in the Simulator class.
 * The hasRefueled flag is used to show that this vehicle does not need to be refueled again.
 * We use the shop time field to dictate how long the driver in this vehicle can spend loitering in the shop we have this here as it is done based on which vehicle the customer has come from, if it is a motorbike, this is always set to 0.
 *
 * @author callummarriage
 */
public class Vehicle implements Customer{

    private Integer maxFuel;
    private Integer currentFuel;
    private Integer timeInQueue;
    private Integer shopPurchase;
    private Integer shopTime;
    private Boolean hasRefueled;
    private Double chanceOfGoingToShop;
    private Double size;
    private Integer maxQueueTime;
    private Boolean isTruck;

    //instead of having a long constructor param list, we could have used the set methods in order to set all these values,
    // the reason we did not do this is to keep the method that creates the vehicles shorter.
    public Vehicle(Integer shopTime, Integer shopPurchase, Integer maxFuel, Double chanceOfGoingToShop, Double size, Integer maxQueueTime){
        this.shopTime = shopTime;
        this.shopPurchase = shopPurchase;
        this.maxFuel = maxFuel;
        this.currentFuel = 0;
        this.timeInQueue = 0;
        this.hasRefueled = false;
        this.chanceOfGoingToShop = chanceOfGoingToShop;
        this.size = size;
        this.maxQueueTime = maxQueueTime;
        isTruck = false;
    }

    public void setIsTruck(){
        this.isTruck = true;
    }

    public Boolean getIsTruck(){
        return isTruck;
    }

    public int getMaxQueueTime() {
        return this.maxQueueTime;
    }

    public Integer getShopTime() {
        return this.shopTime;
    }

    public Integer getShopPurchase() {
        return this.shopPurchase;
    }

    /**
     * Refuels this vehicle, if the vehicle has finished refueling it returns true if not it adds to the current fuel level and returns false
     * @param value the amount that the fuel will increase by
     * @return boolean
     */
    public Boolean act(Integer value) {
        if((this.currentFuel + value ) >= this.maxFuel){
            this.currentFuel = this.maxFuel;
            if(this.hasRefueled){
                return false;
            } else {
                this.hasRefueled = true;
                return true;
            }
        } else{
            this.timeInQueue += 10;
            this.currentFuel += value;
            return false;
        }
    }

    public Integer getTimeInQueue() {
        return this.timeInQueue;
    }

    public Integer getMaxFuel() {
        return this.maxFuel;
    }

    /**
     * Decided to go into the shop based on the speed the vehicle refueled at
     *
     * @param random single random to keep consistency
     * @return whether the driver from this vehicle should go into the shop
     */
    public Boolean decide(Random random) {
        return (getTimeInQueue() < getMaxQueueTime() && random.nextDouble() < getChanceOfGoingToShop());
    }

    private Double getChanceOfGoingToShop() {
        return this.chanceOfGoingToShop;
    }

    public Double getSize() {
        return this.size;
    }

    public void setTimeInQueue(Integer timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

}
