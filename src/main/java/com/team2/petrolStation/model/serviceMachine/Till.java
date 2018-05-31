package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;

import java.util.Queue;

/**
 * Models the till which are used in the Shop.
 *
 * @author callummarriage
 */
public class Till extends AbstractServiceMachine {

    public Till(){
        super();
    }

    /**
     * simulates driver making purchase, if that has completed then it removes the customer from the queue, prints that it has left and returns the customer.
     *
     * @return finished customer
     */
    @Override
    public Customer act() {
        if(getVehiclesInQueue().size() > 0) {
            Driver customer = (Driver) getVehiclesInQueue().peek();

            this.customerQueue.remove();
            System.out.println("Customer left succesfully spending: " + customer.getCurrentSpend());
            return customer;

        }
        return null;
    }

    @Override
    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }
}
