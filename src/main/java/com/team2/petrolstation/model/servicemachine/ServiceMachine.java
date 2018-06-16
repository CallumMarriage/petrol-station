package com.team2.petrolstation.model.servicemachine;

import com.team2.petrolstation.model.customer.Customer;

import java.util.Queue;

/**
 * Interface that allows Tills and Pumps to be treated the same in the facility class.
 *
 * @author callummarriage
 */
public interface ServiceMachine {

    void addCustomer(Customer vehicle);

    Queue<Customer> getCustomersInQueue();

    double getSizeOfCustomersInQueue();

    Customer act();

    void removeCustomer();

    Boolean checkIfCustomerFits(Customer customer);
}
