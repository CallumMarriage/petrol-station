package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Provides implementation of the basic queue functions.
 *
 * @author callummarriage
 */
public abstract class AbstractServiceMachine implements ServiceMachine {

    Queue<Customer> customerQueue;

    AbstractServiceMachine(){
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
            sizeOfVehiclesInQueue += customer.getSize();
        }

        return sizeOfVehiclesInQueue;
    }
}
