package com.team2.petrolStation.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.serviceMachine.Pump;
import com.team2.petrolStation.model.serviceMachine.ServiceMachine;

import java.util.*;

/**
 * Implements the process of assinging a customer to the best available service machine.
 *
 * @author callummarriage
 */
public class Facility {

    ServiceMachine[] customerServers;

    public ServiceMachine[] getCustomersServers() {
            return customerServers;
        }

    public void setCustomerServers(ServiceMachine[] customerServers) {
            this.customerServers = customerServers;
        }

    public Map<Integer, Customer> manageTransactions() throws Exception {
        Map<Integer, Customer> finishedCustomers = new HashMap<>();
        for(int i = 0; i < customerServers.length; i++) {
            Customer customer = customerServers[i].act();
            if(customer != null){
                finishedCustomers.put(i, customerServers[i].act());
            } else {
                throw new Exception("Problem with managing transactions");
            }
        }
        return finishedCustomers;
    }

    public double getFinishedCustomers(Collection<Customer> customers ){

        double lostVehicles = 0.0;
        for (Customer customer : customers) {
            int bestMachine = findBestMachine(customer);
            if(bestMachine < 0){
                if(customer instanceof Vehicle) {
                    Vehicle vehicle = (Vehicle) customer;
                    lostVehicles += vehicle.getMaxFuel() * 0.5;
                } else {
                    System.out.println("ERROR ASSIGNING CUSTOMER TO TILL");
                }
            } else {
                addCustomerToBestMachine(bestMachine, customer);
            }
        }
        return lostVehicles;
    }

    private int findBestMachine(Customer customer) {
        double previous = -1.0;
        int positionOfPumpWithShortestTime = 0;

        for (int i = 0; i < customerServers.length; i++) {
            if (customerServers[i] != null) {
                double pumpQueueSize = customerServers[i].getSizeOfVehiclesInQueue();
                boolean isValid = false;
                if(customer instanceof Driver) {
                    isValid = true;
                } else {
                    Vehicle vehicle = (Vehicle) customer;
                    Pump pump = (Pump) customerServers[i];
                    if((pumpQueueSize + vehicle.getSize()) <= pump.getMaxQueueSize()){
                        isValid = true;
                    }
                }
                if (isValid && ( i == 0 || pumpQueueSize < previous)) {
                    previous = pumpQueueSize;
                    positionOfPumpWithShortestTime = i;
                    if (pumpQueueSize == 0.0) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if(previous >= 0.0){
            return positionOfPumpWithShortestTime;
        } else {
            return -1;
        }
    }

    private void addCustomerToBestMachine(int positionOfPumpWithShortestTime, Customer customer){
        customerServers[positionOfPumpWithShortestTime].addCustomer(customer);
    }


}
