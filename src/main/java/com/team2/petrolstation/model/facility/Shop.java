package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.servicemachine.ServiceMachine;
import com.team2.petrolstation.model.servicemachine.Till;

import java.util.*;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.SECONDS_PER_TICK;

/**
 * The shop contains a map of all of the drivers that are browsing the shop, it also manages the movement of drivers in and out of the shop floor.
 * The shop manages the transactions at all of the tills and should return a map of drivers and their pump.
 *
 * @author callummarriage
 */
public class Shop extends Facility {

    private List<Customer> shopFloor;

    public Shop(Integer numOfServiceMachines){
        //generate the service machines based on the number provided.
        this.customerServers = new ServiceMachine[numOfServiceMachines];
        for(int i = 0; i < numOfServiceMachines; i++){
            this.customerServers[i] = new Till();
        }
        this.shopFloor = new ArrayList<>();
    }

    public void addToShopFloor(List<Driver> drivers){
        this.shopFloor.addAll(drivers);
    }

    public List<Customer> getShopFloor(){
        return this.shopFloor;
    }

    /**
     * simulates drivers spending time loitering in the store.
     *
     * @return a list of customers who have finished shopping.
     */
    public List<Customer> getDriversFinished(){
        List<Customer> finishedDrivers = new ArrayList<>();
        for(Customer driver :  this.shopFloor){
            if(driver.act(SECONDS_PER_TICK)){
                finishedDrivers.add(driver);
            }
        }

        return finishedDrivers;
    }

    public void removeDrivers(List<Customer> customers){
        this.shopFloor.removeAll(customers);
    }
}
