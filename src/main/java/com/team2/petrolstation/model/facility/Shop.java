package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
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
    private Double moneyGained;


    public Shop(Integer numOfServiceMachines){
        super();
        //generate the service machines based on the number provided.
        this.customerServers = new ServiceMachine[numOfServiceMachines];
        for(int i = 0; i < numOfServiceMachines; i++){
            this.customerServers[i] = new Till();
        }
        this.shopFloor = new ArrayList<>();
        this.moneyGained = 0.0;

    }

    /**
     * Simulates the adding of drivers to the shop and the shop to the tills
     *
     * @param finishedAtPump the vehicles that have finished at the pump
     * @param priceOfFuel the price of fuel to calculate how much the driver will spend
     * @param random the random used to generate all of the values
     * @throws PumpNotFoundException returned when the correct pump can not be found.
     */
    public void assignShoppers( Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws PumpNotFoundException {
        addShoppersFromShopFloorToQueue();

        if(finishedAtPump.size() > 0) {
            for(Map.Entry<Integer, Customer> customerAndPump : finishedAtPump.entrySet()){
                sendDriversToNextLocation(customerAndPump, priceOfFuel, random);
            }
        }
    }

    public String removeDriversFromShop(Map.Entry<Integer, Customer> customerEntry ){
        Driver driver = (Driver) customerEntry.getValue();

        addMoneyGained(driver.getCurrentSpend());
        getServiceMachines()[customerEntry.getKey()].removeCustomer();

        return "> A customer has left spending $" +driver.getCurrentSpend() ;
    }

    private void addToShopFloor(Driver driver){
        this.shopFloor.add(driver);
    }

    public List<Customer> getShopFloor(){
        return this.shopFloor;
    }

    /**
     * simulates drivers spending time loitering in the store.
     *
     * @return a list of customers who have finished shopping.
     */
    private List<Customer> getDriversFinished(){
        List<Customer> finishedDrivers = new ArrayList<>();
        for(Customer driver :  this.shopFloor){
            if(driver.act(SECONDS_PER_TICK)){
                finishedDrivers.add(driver);
            }
        }

        return finishedDrivers;
    }

    private void removeDrivers(List<Customer> customers){
        this.shopFloor.removeAll(customers);
    }


    public Double getMoneyGained() {
        return moneyGained;
    }

    private void addMoneyGained(Double moneyToAdd){
        this.moneyGained += moneyToAdd;
    }

    private void addShoppersFromShopFloorToQueue() throws PumpNotFoundException{
        //get drivers who have finished loitering in the shop
        List<Customer> finishedAtShop = getDriversFinished();
        //remove the drivers who are finished from the shopfloor
        removeDrivers(finishedAtShop);
        addCustomersToMachine(finishedAtShop);
    }

    private void sendDriversToNextLocation(Map.Entry<Integer, Customer> customerAndPump, Double priceOfFuel, Random random) throws PumpNotFoundException {
        Vehicle vehicle = (Vehicle) customerAndPump.getValue();
        Integer pumpNumber = customerAndPump.getKey();

        Driver driver = createDriver(vehicle, priceOfFuel, pumpNumber);

        if(vehicle.decide(random)){
            driver.setShopInfo(vehicle);
            addToShopFloor(driver);
        } else {
            addMoneyLost(Double.valueOf(vehicle.getShopPurchase()));
            addCustomerToMachine(driver);
        }
    }

    /**
     * This method provides an interface to the Simulator class to access the facility adding methods
     * @param customers a list of drivers or vehicles to be added to service machines
     * @return the amount of lost vehicles.
     * @throws PumpNotFoundException pump not found
     */
    private void addCustomersToMachine(Collection<Customer> customers ) throws PumpNotFoundException {

        for (Customer customer : customers) {
            addCustomerToMachine(customer);
        }
    }
    private Driver createDriver(Vehicle vehicle, Double priceOfFuel, Integer pumpNumber){
        return new Driver((Math.round((vehicle.getMaxFuel() * priceOfFuel)) * 100d / 100d), pumpNumber);
    }
}
