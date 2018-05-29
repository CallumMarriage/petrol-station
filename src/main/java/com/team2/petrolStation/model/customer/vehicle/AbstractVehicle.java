package com.team2.petrolStation.model.customer.vehicle;

/**
 * Allows for any vehicle to refuel, manage their fuel size and their size.
 *
 * @author callummarriage
 */
public class AbstractVehicle implements Vehicle {

    Integer maxFuel;
    Integer currentFuel;
    Double size;
    Integer timeInQueue;
    Integer shopPurchase;
    Integer shopTime;
    Integer maxQueueTime;


    public Integer getShopTime() {
        return shopTime;
    }

    public void setShopTime(Integer shopTime) {
        this.shopTime = shopTime;
    }

    public Integer getShopPurchase() {
        return shopPurchase;
    }

    public void setShopPurchase(Integer shopPurchase) {
        this.shopPurchase = shopPurchase;
    }

    @Override
    public int getMaxQueueTime() {
        return maxQueueTime;
    }

    @Override
    public void setMaxQueueTimes(int queueTime) {
        this.maxQueueTime = maxQueueTime;
    }

    @Override
    public Boolean act(Integer value) {
        if((this.currentFuel + value ) >= this.maxFuel){
            return true;
        } else{
            this.currentFuel += value;
            return false;
        }
    }

    @Override
    public Double getSize() {
        return this.size;
    }

    @Override
    public void setSize(Double size) {
        this.size = size;
    }

    @Override
    public Integer getTimeInQueue() {
        return this.timeInQueue;
    }

    @Override
    public void setTimeInQueue(Integer time) {
        this.timeInQueue = time;
    }

    @Override
    public void setMaxFuel(Integer maxFuel) {
        this.maxFuel = maxFuel;
    }

    @Override
    public Integer getMaxFuel() {
        return this.maxFuel;
    }
}
