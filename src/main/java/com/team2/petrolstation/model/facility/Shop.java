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

    public String removeDriversFromShop(Map.Entry<Integer, Customer> customerEntry ){
        Driver driver = (Driver) customerEntry.getValue();

        addMoneyGained(driver.getCurrentSpend());
        getServiceMachines()[customerEntry.getKey()].removeCustomer();

        return "> A customer has left spending $" +driver.getCurrentSpend() ;
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


    public Double getMoneyGained() {
        return moneyGained;
    }

    public void setMoneyGained(Double moneyGained) {
        this.moneyGained = moneyGained;
    }

    public void addMoneyGained(Double moneyToAdd){
        this.moneyGained += moneyToAdd;
    }

    /**
     * This method provides an interface to the Simulator class to access the facility adding methods,
     * this reduces coupling by allowing changes to be made to the facility without having to change the Simulator class.
     * Finds and adds each customer to the best free service machine.
     * If a vehicle does not find the best service machine it will leave the Petrol station and possible income will be added to lost money.
     * If a driver does not find the best service machine it will throw an exception.
     *
     * @param customers a list of drivers or vehicles to be added to service machines
     * @return the amount of lost vehicles.
     * @throws PumpNotFoundException pump not found
     */
    private void addCustomersToMachine(Collection<Customer> customers ) throws PumpNotFoundException {

        for (Customer customer : customers) {
            addCustomerToMachine(customer);
        }
    }

    /**
     * Simulates the adding of drivers to the shop and the shop to the tills
     *
     * @param finishedAtPump the vehicles that have finished at the pump
     * @param priceOfFuel the price of fuel to calculate how much the driver will spend
     * @param random the random used to generate all of the values
     * @throws PumpNotFoundException returned when the correct pump can not be found.
     */
    public void assignShoppers( Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws Exception {
        //get drivers who have finished loitering in the shop
        List<Customer> finishedAtShop = getDriversFinished();
        //remove the drivers who are finished from the shopfloor
        removeDrivers(finishedAtShop);
        if(finishedAtPump.size() > 0) {

            //drivers decide if they want to go on the shop floor
            List<Driver> notGoingToShop = new ArrayList<>();
            List<Driver> goingToShop = new ArrayList<>();

            for(Map.Entry<Integer, Customer> customerAndPump : finishedAtPump.entrySet()){

                Vehicle vehicle = (Vehicle) customerAndPump.getValue();
                Integer pumpNumber = customerAndPump.getKey();

                Driver driver = createDriver(vehicle, priceOfFuel, pumpNumber);

                if(vehicle.decide(random)){
                    driver.setShopInfo(vehicle);
                    goingToShop.add(driver);
                } else {
                    addMoneyLost(Double.valueOf(vehicle.getShopPurchase()));
                    notGoingToShop.add(driver);
                }
            }

            //add all of the people who dont want to, to the list of finished drivers
            finishedAtShop.addAll(notGoingToShop);
            //add the drivers who do want to go to the shop floor to it
            addToShopFloor(goingToShop);
        }
        //add the drivers who are finished with the shop floor to tills.
        addCustomersToMachine(finishedAtShop);
    }

    private Driver createDriver(Vehicle vehicle, Double priceOfFuel, Integer pumpNumber){
        return new Driver((Math.round((vehicle.getMaxFuel() * priceOfFuel)) * 100d / 100d), pumpNumber);
    }
}
