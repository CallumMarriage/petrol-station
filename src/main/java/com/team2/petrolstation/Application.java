package com.team2.petrolstation;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import com.team2.petrolstation.model.facility.FillingStation;
import com.team2.petrolstation.model.facility.Shop;
import com.team2.petrolstation.model.view.Simulator;
import com.team2.petrolstation.model.view.ApplicationView;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.*;
import static com.team2.petrolstation.model.constant.VehicleConstants.*;

/**
 * Application class allows for the running of simulations that generate vehicles and then refuels them and sends them to a shop to shop and or purchase their fuel.
 * It also keeps track of the amount of money gained and lost during the simulation.
 * Generates the front end of which allows the user to input values that affect the running of the simulation and displays the results.
 *
 * @author callummarriage canershefik
 */
public class Application implements Simulator{

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private Double chanceOfTruck;
    private Double moneyGained;
    private Double moneyLostFillingStation;
    private Double moneyLostFromShop;
    private Double p;
    private Double q;
    private ApplicationView applicationView;
    private FillingStation fillingStation;
    private Shop shop;

    public static void main(String[] args){

        Double chanceOfTrucks = 0.02;
        new Application(chanceOfTrucks);
    }

    private Application(Double chanceOfTrucks){
        this.moneyLostFromShop = 0.0;
        this.moneyLostFillingStation = 0.0;
        this.moneyGained = 0.0;
        this.chanceOfTruck = chanceOfTrucks;
        this.applicationView = new ApplicationView(this);
        this.applicationView.start();
    }


    /**
     * Simulates the petrol station and calls simulateRound for as long as chosen.
     *
     * @param numOfTurns number of turns in seconds
     * @param numPumps number of pumps
     * @param numTills number of tills
     */
    public void simulate(Integer numOfTurns, Integer numPumps, Integer numTills, Double priceOfFuel, Double p, Double q, Boolean truckIsActive){

        this.p = p;
        this.q = q;

        //build shop, filling station and the random that will be used throughout the application
        this.shop = new Shop(numTills);
        this.fillingStation = new FillingStation(numPumps);
        Random random = new Random(10);

        try {
            for (int i = 0; i < numOfTurns; i++) {
                simulateRound( random, priceOfFuel, truckIsActive);
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Get Results based on performance
     *
     * @return return all of the contents
     */
    public String getResults(){

        //just to make the results look cleaner make sure the right ending word is used
        String vehicle = checkIfPlural(this.fillingStation.getLeftOverCustomers(), "vehicle");
        String driver = checkIfPlural(this.shop.getShopFloor().size(), "driver");
        String tillDrivers = checkIfPlural(this.shop.getLeftOverCustomers(), "driver");

        //finally print out the money lost and gained.
        return START + "\n* Finances *\nMoney lost from Filling Station: $" + this.moneyLostFillingStation +"\nMoney lost from Shop: $" + this.moneyLostFromShop + "\nMoney gained: $" + this.moneyGained + "\n\n* Left over customers *\nFilling Station - Pumps: " + this.fillingStation.getLeftOverCustomers() + " " + vehicle + "\nShop - Tills: "+ this.shop.getLeftOverCustomers() + " " + driver +"\nShop - floor: " + this.shop.getShopFloor().size() + " " + tillDrivers;
    }

    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word += "s";
        }

        return word;
    }

    /**
     * Simulates a single round
     *
     * @param random the random that will be used to generate the vehicles
     * @throws PumpNotFoundException error trying to add customer to a service machine
     */
    private void simulateRound( Random random, Double priceOfFuel, Boolean truckIsActive) throws Exception {

        //create the vehicles for the round and assign them to a pump
        assignVehicles(generateVehicles(random, truckIsActive), priceOfFuel);
        removeCustomers(runShop( runFillingStation(), priceOfFuel, random));
    }

    /**
     * Removes customers from tills and their vehicles pumps once they have finished.
     *
     * @param finishedCustomers customers finished at the shop with their vehicles.
     * @throws PumpNotFoundException pump associated with a customer was not found
     */
    private void removeCustomers(Map<Integer, Customer> finishedCustomers) throws PumpNotFoundException {
        //remove the finished vehicles and shoppers from their pumps
        for (Entry<Integer, Customer> customerEntry : finishedCustomers.entrySet()) {
            Driver driver = (Driver) customerEntry.getValue();
            this.fillingStation.removeCustomerFromPump(driver.getPumpNumber());
            this.moneyGained += driver.getCurrentSpend();
            this.applicationView.updateScreen("A customer has left spending $" + driver.getCurrentSpend());
            this.shop.getServiceMachines()[customerEntry.getKey()].removeCustomer();
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
    private void assignShoppers( Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws Exception {
        //get drivers who have finished loitering in the shop
        List<Customer> finishedAtShop = this.shop.getDriversFinished();
        //remove the drivers who are finished from the shopfloor
        this.shop.removeDrivers(finishedAtShop);
        if(finishedAtPump.size() > 0) {

            //drivers decide if they want to go on the shop floor
            List<Driver> notGoingToShop = new ArrayList<>();
            List<Driver> goingToShop = new ArrayList<>();

            for(Entry<Integer, Customer> customerAndPump : finishedAtPump.entrySet()){

                if(customerAndPump.getValue() instanceof Vehicle) {
                    Vehicle vehicle = (Vehicle) customerAndPump.getValue();
                    Driver driver = new Driver((Math.round((vehicle.getMaxFuel() * priceOfFuel)) * 100d / 100d), customerAndPump.getKey());

                    if (vehicle.decide(random)) {
                        // add the amount that the customer is going to spend in the shop to the shop and add to the total.
                        driver.addToCurrentSpend(vehicle.getShopPurchase());

                        //add the time the driver will be spending in the shop.
                        driver.setTimeInShop(vehicle.getShopTime());

                        //add the customers that are going to the shop to the list.s
                        goingToShop.add(driver);

                    } else {
                        this.moneyLostFromShop += vehicle.getShopPurchase();

                        notGoingToShop.add(driver);
                    }
                } else {
                    throw new Exception("Something went wrong!");
                }
            }

            //add all of the people who dont want to, to the list of finished drivers
            finishedAtShop.addAll(notGoingToShop);
            //add the drivers who do want to go to the shop floor to it
            this.shop.addToShopFloor(goingToShop);
        }
        //add the drivers who are finished with the shop floor to tills.
        this.shop.addCustomerToMachine(finishedAtShop);
    }


    /**
     * Generates vehicles and assigns the new vehicles to the best pump
     *
     * @param priceOfFuel price of fuel
     * @param vehicles all the new vehicles
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private void assignVehicles( Collection<Customer> vehicles, Double priceOfFuel) throws PumpNotFoundException {

        //if vehicles have been generated, add them to the queues
        if(!vehicles.isEmpty()){
            Collection<Customer> lostCustomers = this.fillingStation.addCustomerToMachine(vehicles);
            this.moneyLostFillingStation += calculateLossPerVehicle(lostCustomers, priceOfFuel);
        }
    }

    /**
     * Manages the transactions for the filling station, then sets the chance of trucks arriving based on this information.
     *
     * @return finished drivers with the pump number they have left behind
     */
    private Map<Integer, Customer> runFillingStation() {
        //Get the finished vehicles in the system
        Map<Integer, Customer> finishedAtPump = this.fillingStation.manageTransactions();

        //if there are finished vehicles set the chance of trucks spawning
        if(finishedAtPump.size() > 0) {
            setChanceOfTruck(finishedAtPump.values());
        }

        return finishedAtPump;
    }

    /**
     * gets finished drivers from the shop and assigned new drivers to their relevant places in the shop (the queue for the till or the shop floor)
     *
     *  @param finishedAtPump drivers and their service machines
     * @param priceOfFuel price of fuel
     * @param random random
     * @return drivers who are finished and the pump where their vehicle is
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private Map<Integer, Customer> runShop(Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws Exception {
        //get the finished shoppers in the system
        Map<Integer, Customer> finishedCustomers = shop.manageTransactions();
        //add the new shoppers to the correct locations
        assignShoppers(finishedAtPump, priceOfFuel, random);
        return finishedCustomers;
    }

    /**
     * Calculates the lost money from vehicles not finding a pump
     *
     * @param lostCustomers customers
     * @param priceOfFuel price of fuel
     * @return amount of money lost
     */
    private Double calculateLossPerVehicle(Collection<Customer> lostCustomers, Double priceOfFuel){
        Double moneyLost = 0.0;
        for(Customer customer : lostCustomers){
            applicationView.updateScreen("A Customer had to leave!");
            moneyLost += Math.round((((Vehicle)customer ).getMaxFuel() * priceOfFuel)*100/100);
        }
        return moneyLost;
    }

    /**
     * generates vehicles
     *
     * @param random random
     * @param truckIsActive checks if truck is active, this can turned off at start
     * @return list of generated vehicles
     */
    private List<Customer> generateVehicles(Random random, Boolean truckIsActive){

        List<Customer> vehicles = new ArrayList<>();
        Double randomNum = random.nextDouble();

        try {
            if (randomNum > p && randomNum <= (2 * p)) {
                vehicles.add(new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0));
                this.applicationView.updateScreen(MOTORBIKE_ARRIVED);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }

            if (randomNum <= p) {
                vehicles.add(new Vehicle(random.nextInt(24 - 12 + 1) + 12, random.nextInt(10 -5 + 1) + 5, random.nextInt(9 - 7  + 1) + 7, CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR));
                this.applicationView.updateScreen(SMALL_CAR_ARRIVED);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }

            
            if (truckIsActive && randomNum > ((2 * p) + q) && randomNum <= (((2 * p) + q) + chanceOfTruck)) {
                Vehicle vehicle = new Vehicle(random.nextInt(36 -24 +1  ) +24, random.nextInt(20 - 15 + 1) + 15, random.nextInt(40 - 30 + 1) + 30, CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
                vehicle.setIsTruck();
                vehicles.add(vehicle);
                this.applicationView.updateScreen(TRUCK_ARRIVED);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
              
            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                vehicles.add(new Vehicle(random.nextInt(30 - 12 + 1) + 12, random.nextInt(16 - 8 + 1) + 8, random.nextInt(18) + 12, CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP, SIZE_OF_FAMILY_SEDAN, MAX_QUEUE_TIME_SMALL_CAR));
                this.applicationView.updateScreen(FAMILY_SEDAN);
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return vehicles;
    }

    /**
     * Sets the chance of a truck being generated based on current trucks experience.
     *
     * @param customers list of all the customers
     */
    private void setChanceOfTruck(Collection<Customer> customers){
        for(Customer customer : customers){
            Vehicle vehicle = (Vehicle) customer;
            if(vehicle.getIsTruck()){
                if(vehicle.getTimeInQueue() < vehicle.getMaxQueueTime()){
                    this.chanceOfTruck += ((this.chanceOfTruck / 100) * 5);
                } else {
                    this.chanceOfTruck -= ((this.chanceOfTruck / 100) * 20) ;
                }
            }
        }
    }

}