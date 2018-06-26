package com.team2.petrolstation.model.customer;

import java.util.Random;

/**
 * Allows for any vehicle to refuel, manage their fuel size and their size.
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

    public Boolean decide(Random random) {
        return (getTimeInQueue() < getMaxQueueTime() && random.nextDouble() < getChanceOfGoingToShop());
    }

    public Double getChanceOfGoingToShop() {
        return this.chanceOfGoingToShop;
    }

    public Double getSize() {
        return this.size;
    }
}
