package com.team2.petrolstation;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import com.team2.petrolstation.model.facility.FillingStation;
import com.team2.petrolstation.model.facility.Shop;
import com.team2.petrolstation.util.VehicleGeneratorUtils;
import com.team2.petrolstation.view.SimulatorController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.*;
import static com.team2.petrolstation.model.constant.PetrolStationConstants.FAMILY_SEDAN;

/**
 * Simulator class allows for the running of simulations that generate vehicles and then refuels them and sends them to a shop to shop and or purchase their fuel.
 * It also keeps track of the amount of money gained and lost during the simulation.
 * Generates the front end of which allows the user to input values that affect the running of the simulation and displays the results.
 *
 * @author callummarriage canershefik
 */
public class Simulator {

    private static final Logger LOGGER = Logger.getLogger(Simulator.class.getName());

    private FillingStation fillingStation;
    private Shop shop;
    private SimulatorController simulatorController;

    private Double p;
    private Double q;
    private Double chanceOfTruck;

    @FXML
    private TextArea textArea;

    public Simulator(int numPumps, int numTills, TextArea textArea, Double p, Double q, Double chanceOfTruck){
        this.shop = new Shop(numTills);
        this.fillingStation = new FillingStation(numPumps);
        this.simulatorController = new SimulatorController();
        this.textArea = textArea;
        this.p = p;
        this.q = q;
        this.chanceOfTruck = chanceOfTruck;
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
     * @param priceOfFuel price of fuel
     * @throws PumpNotFoundException error trying to add customer to a service machine
     *
     * @return string of round
     */
    private String simulateRound( Random random, Double priceOfFuel) throws Exception {

        //create the vehicles for the round and assign them to a pump
        String assigned = generateVehicles(random, priceOfFuel);
        String removed = removeCustomers(runShop( runFillingStation(), priceOfFuel, random));

        if(!assigned.equals("") && !removed.equals("")){
            assigned += "\n";
        }
        return assigned + removed;
    }

    /**
     * generates vehicles
     *
     * @param random random
     * @return list of generated vehicles
     */
    public String generateVehicles(Random random, Double priceOfFuel){

        Double randomNum = random.nextDouble();
        StringBuilder output = new StringBuilder();

        try {
            if (randomNum > p && randomNum <= (2 * p)) {
                output.append(MOTORBIKE_ARRIVED +"\n");

                Vehicle motorbike = VehicleGeneratorUtils.generateMotorbike();
                output.append(this.fillingStation.addVehicleToPetrolStation(motorbike, priceOfFuel));
            }

            if (randomNum <= p) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a small car and add it to the list of generated vehicles
                output.append(SMALL_CAR_ARRIVED + "\n");

                Vehicle smallCar = VehicleGeneratorUtils.generateSmallCar(random);
                output.append(this.fillingStation.addVehicleToPetrolStation(smallCar, priceOfFuel));

            }

            if (randomNum > ((2 * p) + q) && randomNum <= (((2 * p) + q) + chanceOfTruck)) {
                output.append(TRUCK_ARRIVED + "\n");

                Vehicle truck = VehicleGeneratorUtils.generateTruck(random);
                output.append(this.fillingStation.addVehicleToPetrolStation(truck, priceOfFuel));

            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a family sedan and add it to the list of generated vehicles
                output.append(FAMILY_SEDAN + "\n");

                Vehicle sedan = VehicleGeneratorUtils.generateFamilySedan(random);
                output.append(this.fillingStation.addVehicleToPetrolStation(sedan, priceOfFuel));

            }
        }catch (Exception e){
            // LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return output.toString();
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
        this.shop.assignShoppers(finishedAtPump, priceOfFuel, random);
        return finishedCustomers;
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
            this.fillingStation.removeCustomerFromPump(((Driver) customerEntry.getValue()).getPumpNumber());
            sb.append(this.shop.removeDriversFromShop(customerEntry));
        }
        return sb.toString();
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
        return START + "\n* Finances *\nMoney lost from Filling Station: $" + this.fillingStation.getMoneyLost() +
                "\nMoney lost from Shop: $" + this.shop.getMoneyLost() +
                "\nMoney gained: $" + this.shop.getMoneyGained() +
                "\n\n* Left over customers *\nFilling Station - Pumps: " + this.fillingStation.getLeftOverCustomers() + " " + vehicle +
                "\nShop - Tills: "+ this.shop.getLeftOverCustomers() + " " + driver +
                "\nShop - floor: " + this.shop.getShopFloor().size() + " " + tillDrivers;
    }


    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word += "s";
        }

        return word;
    }

    public FillingStation getFillingStation() {
        return fillingStation;
    }

    public Shop getShop() {
        return shop;
    }
}