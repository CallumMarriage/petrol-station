package com.team2.petrolstation.model.servicemachine;

import com.team2.petrolstation.model.customer.Customer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Provides implementation of the basic queue functions.
 *
 * @author callummarriage
 */
public abstract class AbstractServiceMachine implements ServiceMachine {

    private Queue<Customer> customerQueue;

    AbstractServiceMachine(){
        customerQueue = new LinkedList<>();
    }

    @Override
    public Queue<Customer> getCustomersInQueue(){
        return customerQueue;
    }

    @Override
    public double getSizeOfCustomersInQueue(){
        double sizeOfVehiclesInQueue = 0;

        for(Customer customer : customerQueue){
            sizeOfVehiclesInQueue += customer.getSize();
        }

        return sizeOfVehiclesInQueue;
    }

    @Override
    public void removeCustomer(){
        customerQueue.remove();
    }

    /**
     * Add customer to queue but check if it fits
     * @param vehicle adds customer to queue
     */
    @Override
    public void addCustomer(Customer vehicle) {
        customerQueue.add(vehicle);
    }

}
