package com.team2.petrolStation.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.serviceMachine.Pump;
import com.team2.petrolStation.model.serviceMachine.ServiceMachine;

import java.util.*;

import static com.team2.petrolStation.model.constants.ServiceMachineConstants.MAX_QUEUE_SIZE;

/**
 * Implements the process of assinging a customer to the best available service machine.
 *
 * @author callummarriage
 */
public class Facility {

    ServiceMachine[] customerServers;

    /**
     * Simulates a facility processing the customers at the front of the queues and adds those that have finished to a hashmap allong with the SM they have come from.
     *
     * @return a map of customers to their service machines.
     */
    public Map<Integer, Customer> manageTransactions() {
        Map<Integer, Customer> finishedCustomers = new HashMap<>();
        for(int i = 0; i < customerServers.length; i++) {
            Customer customer = customerServers[i].act();
            if(customer != null){
                finishedCustomers.put(i, customer);
            }
        }
        return finishedCustomers;
    }

    /**
     * Finds and adds each customer to the best free service machine.
     * If a vehicle does not find the best service machine it will leave the Petrol station and possible income will be added to lost money.
     * If a driver does not find the best service machine it will throw an exception.
     *
     * @param customers a list of drivers or vehicles to be added to service machines
     * @return the amount of lost vehicles.
     */
    public double addCustomerToMachine(Collection<Customer> customers,Double priceOfFuel ) throws ServiceMachineAssigningException {

        double lostVehicles = 0.0;
        for (Customer customer : customers) {
            int bestMachine = findBestMachine(customer);
            if(bestMachine < 0){
                if(customer instanceof Vehicle) {
                    Vehicle vehicle = (Vehicle) customer;
                    lostVehicles += (vehicle.getMaxFuel() * priceOfFuel) * 100d /100d;
                    System.out.println("A customer had to leave");
                } else {
                    throw new ServiceMachineAssigningException();
                }
            } else {
                addCustomerToBestMachine(bestMachine, customer);
            }
        }
        return lostVehicles;
    }

    /**
     * Finds the best machine available for each customer.
     *
     * @param customer can be a driver or a vehicle
     * @return the position of the best service machine.
     */
     public int findBestMachine(Customer customer) {
        double previous = -1.0;
        int positionOfPumpWithShortestTime = 0;

        for (int i = 0; i < customerServers.length; i++) {
            if (customerServers[i] != null) {
                double pumpQueueSize = customerServers[i].getSizeOfCustomersInQueue();
                boolean isValid = false;
                if(customer instanceof Driver) {
                    isValid = true;
                } else {
                    Vehicle vehicle = (Vehicle) customer;
                    Pump pump = (Pump) customerServers[i];
                    if((pumpQueueSize + vehicle.getSize()) <= MAX_QUEUE_SIZE){
                        isValid = true;
                    }
                }
                if (isValid && ( i == 0 || previous == -1.0 || pumpQueueSize < previous)) {
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

    public void addCustomerToBestMachine(int positionOfPumpWithShortestTime, Customer customer){
        customerServers[positionOfPumpWithShortestTime].addCustomer(customer);
    }

    public ServiceMachine[] getServiceMachines(){
        return this.customerServers;
    }

    /**
     * Adds the amount of all of the drivers at all of the queues and returns them.
     *
     * @return the amount of customers in the facility.
     */
    public Integer printLeftOverCustomers(){
        Integer numCustomers = 0;
        for(ServiceMachine pump : getServiceMachines()){
            numCustomers += pump.getCustomersInQueue().size();
        }
        return numCustomers;
    }
}
