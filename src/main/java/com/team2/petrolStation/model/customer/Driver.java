package com.team2.petrolStation.model.customer;

import com.team2.petrolStation.model.customer.vehicle.Motorbike;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;

/**
 * The driver is the model of a customer who enters the shop and purchases the fuel that they have just put in their vehicle.
 * The driver remembers how much they have spent and contains an act method that inputs a value and decides whether they have finished in the shop.
 *
 * @author callummarriage
 */
public class Driver implements Customer {

    private Integer currentSpend;
    private Integer timeInShop;
    private Integer currentTimeInShop;
    private Integer maximumSpend;

    public Driver(Integer currentSpend ){
        this.currentSpend = currentSpend;
        this.timeInShop = 0;
        this.maximumSpend = 0;
        this.currentTimeInShop = 0;
    }

    @Override
    public Boolean act(Integer value) {
        if((currentTimeInShop + value) >= timeInShop){
            return true;
        } else {
            this.currentTimeInShop += value;
            return false;
        }
    }

    public Integer getTimeInShop() {
        return timeInShop;
    }

    public void setTimeInShop(Integer timeInShop) {
        this.timeInShop = timeInShop;
    }

    public boolean decideToGoToShop(Vehicle vehicle){
        if( vehicle instanceof Motorbike ){
            return false;
        }
        if(vehicle.getTimeInQueue() < vehicle.getMaxQueueTime()){
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentSpend(Integer spendInShop){
        this.currentSpend += spendInShop;
    }

    public double getCurrentSpend(){
        return this.currentSpend;
    }

    public Integer getMaximumSpend() {
        return maximumSpend;
    }

    public void setMaximumSpend(Integer maximumSpend) {
        this.maximumSpend = maximumSpend;
    }


}
