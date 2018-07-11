package com.team2.petrolstation.model.servicemachine;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Vehicle;

import static com.team2.petrolstation.model.constant.ServiceMachineConstants.MAX_QUEUE_SIZE;

/**
 * Allows for a maximum number of vehicles in its queue.
 *
 * @author callummarriage
 */
public class Pump extends AbstractServiceMachine {

    public Pump(){
        super();
    }

    /**
     * Refuels customer for one round and returns the customer if it has finished.
     *
     * @return The finished customer or null if the customer has not finished
     */
    @Override
    public Customer act() {
        if(!getCustomersInQueue().isEmpty()) {
            Customer customer = getCustomersInQueue().peek();
            if (customer.act(1)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public Boolean checkIfCustomerFits(Customer customer) {
        Vehicle vehicle = (Vehicle) customer;
        return ((getSizeOfCustomersInQueue() + vehicle.getSize()) <= MAX_QUEUE_SIZE);
    }
}
