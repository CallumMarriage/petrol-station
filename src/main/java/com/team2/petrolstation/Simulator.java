package com.team2.petrolstation;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import com.team2.petrolstation.model.facility.FillingStation;
import com.team2.petrolstation.model.facility.Shop;
import com.team2.petrolstation.view.SimulatorController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.*;
import static com.team2.petrolstation.model.constant.VehicleConstants.*;

/**
 * Simulator class allows for the running of simulations that generate vehicles and then refuels them and sends them to a shop to shop and or purchase their fuel.
 * It also keeps track of the amount of money gained and lost during the simulation.
 * Generates the front end of which allows the user to input values that affect the running of the simulation and displays the results.
 *
 * @author callummarriage canershefik
 */
public class Simulator {

    private static final Logger LOGGER = Logger.getLogger(Simulator.class.getName());

    private Double chanceOfTruck;
    private Double moneyGained;
    private Double moneyLostFillingStation;
    private Double moneyLostFromShop;
    private Double p;
    private Double q;
    private FillingStation fillingStation;
    private Shop shop;

    private SimulatorController simulatorController;

    @FXML
    private TextArea textArea;

    public Simulator(int numPumps, int numTills, TextArea textArea){
        this.shop = new Shop(numTills);
        this.fillingStation = new FillingStation(numPumps);
        this.moneyLostFromShop = 0.0;
        this.moneyLostFillingStation = 0.0;
        this.moneyGained = 0.0;
        this.chanceOfTruck = 0.02;
        this.simulatorController = new SimulatorController();
        this.textArea = textArea;
    }

    public void simulate(Integer numOfTurns, Double intPrice){
        Random random = new Random(8);

        try {
            for (int i = 0; i < numOfTurns; i++) {
                String round = simulateRound( random, intPrice);
                if(!round.equals("")){
                    simulatorController.updateScreen(round, textArea);
                }
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }
    /**
     * Simulates a single round
     *
     * @param random the random that will be used to generate the vehicles
     * @throws PumpNotFoundException error trying to add customer to a service machine
     */
    public String simulateRound( Random random, Double priceOfFuel) throws Exception {

        //create the vehicles for the round and assign them to a pump
        String assigned = assignVehicles(generateVehicles(random), priceOfFuel);
        String removed = removeCustomers(runShop( runFillingStation(), priceOfFuel, random));

        if(!assigned.equals("") && !removed.equals("")){
            assigned += "\n";
        }
        return assigned + removed;
    }

    /**
     * Generates vehicles and assigns the new vehicles to the best pump
     *
     * @param priceOfFuel price of fuel
     * @param vehicles all the new vehicles
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private String assignVehicles( Collection<Customer> vehicles, Double priceOfFuel) throws PumpNotFoundException {

        //if vehicles have been generated, add them to the queues
        if(!vehicles.isEmpty()){
            Collection<Customer> lostCustomers = this.fillingStation.addCustomerToMachine(vehicles);
            return calculateLossPerVehicle(lostCustomers, priceOfFuel);
        }

        return "";
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
     * @param finishedAtPump drivers and their service machines
     * @param priceOfFuel price of fuel
     * @param random random
     * @return drivers who are finished and the pump where their vehicle is
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private Map<Integer, Customer> runShop(Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws Exception {
        //get the finished shoppers in the system first so that they do not interfere with the new shoppers
        Map<Integer, Customer> finishedCustomers = shop.manageTransactions();
        //add the new shoppers to the correct locations, some shoppers will go to the shop floor, others straight to the tills.
        assignShoppers(finishedAtPump, priceOfFuel, random);
        return finishedCustomers;
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
     * Removes customers from tills and their vehicles pumps once they have finished.
     *
     * @param finishedCustomers customers finished at the shop with their vehicles.
     * @throws PumpNotFoundException pump associated with a customer was not found
     */
    private String removeCustomers(Map<Integer, Customer> finishedCustomers) throws PumpNotFoundException {
        StringBuilder sb = new StringBuilder();
        //remove the finished vehicles and shoppers from their pumps
        for (Entry<Integer, Customer> customerEntry : finishedCustomers.entrySet()) {
            Driver driver = (Driver) customerEntry.getValue();
            this.fillingStation.removeCustomerFromPump(driver.getPumpNumber());
            this.moneyGained += driver.getCurrentSpend();
            sb.append("A customer has left spending $");
            sb.append(driver.getCurrentSpend());
            this.shop.getServiceMachines()[customerEntry.getKey()].removeCustomer();
        }
        return sb.toString();
    }

    /**
     * Calculates the lost money from vehicles not finding a pump
     *
     * @param lostCustomers customers
     * @param priceOfFuel price of fuel
     * @return amount of money lost
     */
    private String calculateLossPerVehicle(Collection<Customer> lostCustomers, Double priceOfFuel){
        StringBuilder stringBuilder = new StringBuilder();
        for(Customer customer : lostCustomers){
            stringBuilder.append("A Customer had to leave!");
            this.moneyLostFillingStation += Math.round((((Vehicle)customer ).getMaxFuel() * priceOfFuel)*100/100);
        }
        return stringBuilder.toString();
    }

    /**
     * generates vehicles
     *
     * @param random random
     * @return list of generated vehicles
     */
    private List<Customer> generateVehicles(Random random){

        List<Customer> vehicles = new ArrayList<>();
        Double randomNum = random.nextDouble();

        try {
            if (randomNum > p && randomNum <= (2 * p)) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a motorbike and add it to the list of generated vehicles
                vehicles.add(new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0));
                //simulatorController.updateScreen(MOTORBIKE_ARRIVED, textArea);
            }

            if (randomNum <= p) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a small car and add it to the list of generated vehicles
                vehicles.add(new Vehicle(random.nextInt(24 - 12 + 1) + 12, random.nextInt(10 -5 + 1) + 5, random.nextInt(9 - 7  + 1) + 7, CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR));
                //simulatorController.updateScreen(SMALL_CAR_ARRIVED, textArea);
            }

            if (randomNum > ((2 * p) + q) && randomNum <= (((2 * p) + q) + chanceOfTruck)) {
                //create a Truck and add it to the list of generated vehicles
                Vehicle vehicle = new Vehicle(random.nextInt(36 -24 +1  ) +24, random.nextInt(20 - 15 + 1) + 15, random.nextInt(40 - 30 + 1) + 30, CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //tell that its a truck
                vehicle.setIsTruck();
                vehicles.add(vehicle);
                //simulatorController.updateScreen(TRUCK_ARRIVED, textArea);

            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a family sedan and add it to the list of generated vehicles
                vehicles.add(new Vehicle(random.nextInt(30 - 12 + 1) + 12, random.nextInt(16 - 8 + 1) + 8, random.nextInt(18) + 12, CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP, SIZE_OF_FAMILY_SEDAN, MAX_QUEUE_TIME_FAMILY_SEDAN));
               // simulatorController.updateScreen(FAMILY_SEDAN, textArea);
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);

            }
        }catch (Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage());          
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
                    this.chanceOfTruck -= ((this.chanceOfTruck / 100) * 20);
                }
            }
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

    public Double getMoneyLostFromShop(){
        return this.moneyLostFromShop;
    }

    public Double getMoneyLostFillingStation(){
        return this.moneyLostFillingStation;
    }

    public Double getMoneyGained(){
        return this.moneyGained;
    }

    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word += "s";
        }

        return word;
    }

    public void setP(Double p){
        this.p = p;
    }

    public void setQ(Double q){
        this.q = q;
    }

    public void setChanceOfTruck(Double t){
        this.chanceOfTruck = t;
    }
}