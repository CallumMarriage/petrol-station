package com.team2.petrolstation.model.servicemachine;

import com.team2.petrolstation.model.customer.Customer;

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
        if(!getCustomersInQueue().isEmpty()) {
            return getCustomersInQueue().peek();
        }
        return null;
    }

    @Override
    public Boolean checkIfCustomerFits(Customer customer) {
        return true;
    }
}
