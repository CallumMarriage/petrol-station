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
    }

    @Override
    public Customer act() {
        if(getVehiclesInQueue().size() > 0) {
            Driver customer = (Driver) getVehiclesInQueue().peek();
            if (customer.act(1)) {
                this.customerQueue.remove();
                System.out.println("Customer left succesfully spending: " + customer.getCurrentSpend());
                return customer;
            }
        }
        return null;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        customerQueue.add(customer);
        return true;
    }
}
