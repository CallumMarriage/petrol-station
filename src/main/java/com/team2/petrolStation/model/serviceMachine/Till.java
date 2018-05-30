package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;

import java.util.Queue;

/**
 * Models the till which are used in the Shop.
 *
 * @author callummarriage
 */
public class Till extends AbstractServiceMachine {

    public Till(){
    }

    @Override
    public Customer act() {
        if(getVehiclesInQueue().size() > 0) {
            Customer customer = getVehiclesInQueue().peek();
            if (customer.act(1)) {
                this.customerQueue.remove();
                System.out.println("Customer left succesfully");
                return customer;
            }
        }
        return null;
    }

    @Override
    public boolean addCustomer(Customer vehicle) {

        return true;
    }
}
