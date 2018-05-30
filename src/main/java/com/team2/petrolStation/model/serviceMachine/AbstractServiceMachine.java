package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Provides implementation of the basic queue functions.
 *
 * @author callummarriage
 */
public abstract class AbstractServiceMachine implements ServiceMachine {

    Queue<Customer> customerQueue;

    public AbstractServiceMachine(){
        customerQueue = new LinkedList<>();
    }

    @Override
    public Queue<Customer> getVehiclesInQueue(){
        return customerQueue;
    }

    @Override
    public double getSizeOfVehiclesInQueue(){
        double sizeOfVehiclesInQueue = 0;

        for(Customer customer : customerQueue){
            Vehicle vehicle = (Vehicle) customer;
            sizeOfVehiclesInQueue+= vehicle.getSize();
        }

        return sizeOfVehiclesInQueue;
    }

    @Override
    public boolean removeVehicle(){
        if(customerQueue.size() > 0) {
            customerQueue.remove();
            return true;
        } else {
            return false;
        }
    }
}
