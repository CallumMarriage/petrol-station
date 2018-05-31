package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

import static com.team2.petrolStation.model.constants.ServiceMachineConstants.MAX_QUEUE_SIZE;

/**
 * Allows for a maximum number of vehicles in its queue.
 *
 * @author callummarriage
 */
public class Pump extends AbstractServiceMachine {

    public Pump(){
        super();
    }

    @Override
    public void addCustomer(Customer vehicle) {

        if((getSizeOfVehiclesInQueue() + vehicle.getSize()) <= MAX_QUEUE_SIZE){
            customerQueue.add(vehicle);
        }
    }

    /**
     * Refuels customer for one round and returns the customer if it has finished.
     *
     * @return The finished customer or null if the customer has not finished
     */
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
}
