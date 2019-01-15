package com.team2.petrolstation;

import com.team2.petrolstation.model.PetrolStation;
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

    private SimulatorController simulatorController;

    private Double p;
    private Double q;
    private Double chanceOfTruck;

    private PetrolStation petrolStation;

    @FXML
    private TextArea textArea;

    public Simulator(int numPumps, int numTills, Double p, Double q, Double chanceOfTruck, TextArea textArea){
        this.petrolStation = new PetrolStation(numTills, numPumps);
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
            //        System.out.println(round);
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

        Map<Integer, Customer> finishedAtPump = petrolStation.runFillingStation();

        //if there are finished vehicles set the chance of trucks spawning
        if(finishedAtPump.size() > 0) {
            setChanceOfTruck(finishedAtPump.values());
        }

        String removed = this.petrolStation.removeCustomers(this.petrolStation.runShop( finishedAtPump, priceOfFuel, random));
        assigned = assigned.trim();
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
                output.append(this.petrolStation.addVehicleToPetrolStation(motorbike, priceOfFuel));
            }

            if (randomNum <= p) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a small car and add it to the list of generated vehicles
                output.append(SMALL_CAR_ARRIVED + "\n");

                Vehicle smallCar = VehicleGeneratorUtils.generateSmallCar(random);
                output.append(this.petrolStation.addVehicleToPetrolStation(smallCar, priceOfFuel));

            }

            if (randomNum > ((2 * p) + q) && randomNum <= (((2 * p) + q) + chanceOfTruck)) {
                output.append(TRUCK_ARRIVED + "\n");

                Vehicle truck = VehicleGeneratorUtils.generateTruck(random);
                output.append(this.petrolStation.addVehicleToPetrolStation(truck, priceOfFuel));

            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                //TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                //create a family sedan and add it to the list of generated vehicles
                output.append(FAMILY_SEDAN + "\n");

                Vehicle sedan = VehicleGeneratorUtils.generateFamilySedan(random);
                output.append(this.petrolStation.addVehicleToPetrolStation(sedan, priceOfFuel));

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

    public String getFinalResults(){
        return this.petrolStation.getResults();
    }

    public PetrolStation getPetrolStation(){
        return this.petrolStation;
    }
}