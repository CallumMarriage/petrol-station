package com.team2.petrolStation.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;

import java.util.List;
import java.util.Queue;

/**
 * Interface that allows Tills and Pumps to be treated the same in the facility class.
 *
 * @author callummarriage
 */
public interface ServiceMachine {
    void addCustomer(Customer vehicle);

    Queue<Customer> getVehiclesInQueue();

    boolean removeVehicle();

    double getSizeOfVehiclesInQueue();

    Customer act();
}
