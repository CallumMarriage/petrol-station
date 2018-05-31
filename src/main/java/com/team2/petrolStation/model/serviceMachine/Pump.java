package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;

import java.util.ArrayList;
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

        if((getSizeOfVehiclesInQueue() + vehicle.getSize()) > maxQueueSize){
            return false;
        } else {
            customerQueue.add(vehicle);
            return true;
        }
    }

    @Override
    public Customer act() {
        if(getVehiclesInQueue().size() > 0) {
            Customer customer = getVehiclesInQueue().peek();
            if (customer.act(1)) {
                return customer;
            }
        }
        return null;
    }

    public Double getMaxQueueSize() {
        return maxQueueSize;
    }
}
