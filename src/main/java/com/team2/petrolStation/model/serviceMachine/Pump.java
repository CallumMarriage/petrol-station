package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;

import java.util.List;

/**
 * Allows for a maximum number of vehicles in its queue.
 *
 * @author callummarriage
 */
public class Pump extends AbstractServiceMachine {

    private Double maxQueueSize;

    public Pump(){
        super();
        maxQueueSize = 3.5;
    }

    @Override
    public boolean addCustomer(Customer vehicle) {
        return false;
    }

    @Override
    public Customer act() {
        return null;
    }

    public Double getMaxQueueSize() {
        return maxQueueSize;
    }
}
