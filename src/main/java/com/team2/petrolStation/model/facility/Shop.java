package com.team2.petrolStation.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Motorbike;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.serviceMachine.ServiceMachine;
import com.team2.petrolStation.model.serviceMachine.Till;

import java.util.*;

/**
 * The shop contains a map of all of the drivers that are browsing the shop, it also manages the movement of drivers in and out of the shop floor.
 * The shop manages the transactions at all of the tills and should return a map of drivers and their pump.
 *
 * @author callummarriage
 */
public class Shop extends Facility {

    private List<Driver> shopFloor;

    public Shop(Integer numOfServiceMachines){
        customerServers = new ServiceMachine[numOfServiceMachines];
        for(int i = 0; i < numOfServiceMachines; i++){
            customerServers[i] = new Till();
        }
        shopFloor = new ArrayList<>();
    }

    public List<List<Driver>> decideToGoToShop(Map <Integer, Customer> customers, Random random){
        List<Driver> customersNotGoingToShop = new ArrayList<>();
        List<Driver> customersGoingToShop = new ArrayList<>();
        for(Integer pump : customers.keySet()){

            Vehicle vehicle = (Vehicle) customers.get(pump);
            Driver driver = new Driver(vehicle.getMaxFuel(), pump);

            if( vehicle instanceof Motorbike){

                customersNotGoingToShop.add(driver);
            } else {
                if (vehicle.getTimeInQueue() < vehicle.getMaxQueueTime() && random.nextDouble() < vehicle.getChanceOfGoingToShop()) {

                    driver.setMaximumSpend(vehicle.getMaxFuel() + vehicle.getShopPurchase());
                    customersGoingToShop.add(driver);

                } else {
                    customersNotGoingToShop.add(driver);
                }
            }
        }

        List<List<Driver>> allCustomers = new ArrayList<List<Driver>>();
        allCustomers.add(customersNotGoingToShop);
        allCustomers.add(customersGoingToShop);

        return allCustomers;
    }

    public void addToShopFloor(List<Driver> drivers){
        shopFloor.addAll(drivers);
    }

    public List<Customer> getDriversFinished(){
        List<Customer> finishedDrivers = new ArrayList<Customer>();
        for(Driver driver :  shopFloor){
            if(driver.act(10)){
                finishedDrivers.add(driver);
            }
        }

        return finishedDrivers;
    }
}
