package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.InvalidInputException;
import com.team2.petrolStation.model.exceptions.PumpNotFoundException;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.RESULTS_DESTINATION_FILE;
import static com.team2.petrolStation.model.constants.PetrolStationConstants.SECONDS_PER_TICK;

/**
 * Application class allows for the running of simulations that generate vehicles and then refuels them and sends them to a shop to shop and or purchase their fuel.
 * It also keeps track of the amount of money gained and lost during the simulation.
 * Generates the front end of which allows the user to input values that affect the running of the simulation and displays the results.
 *
 * @author callummarriage
 */
public class Application {

    private Double chanceOfTruck;
    private Double moneyGained;
    private Double moneyLost;
    private static Double p;
    private static Double q;

    public static void main(String[] args){
        Double chanceOfTrucks = 0.02;
        Application application = new Application(chanceOfTrucks);

        //replace this with gui values
        Integer numOfTurns = 0;
        Integer numPumps = 1;
        Integer numTills = 1;
        Double priceOfFuel = 1.2;
        p = 0.01;
        q = 0.02;

        String time = "5";
        String identifiers = "h";

        try {
            numOfTurns = convertTimeIntoSeconds(time, identifiers);

        } catch(InvalidInputException e){
            e.printStackTrace();
            return;
        }

        System.out.println("Welcome to the simulation\nThe duration will be " + numOfTurns + " seconds or " + (numOfTurns / SECONDS_PER_TICK) + " ticks \n");

        //run the simulation using the inputed values
        application.simulate(numOfTurns, numPumps, numTills, priceOfFuel);
    }

    public Application(Double chanceOfTrucks){
        moneyLost = 0.0;
        moneyGained = 0.0;
        this.chanceOfTruck = chanceOfTrucks;
    }

    /**
     * Simulates the petrol station and calls simulateRound for as long as chosen.
     *
     * @param numOfTurns number of turns in seconds
     * @param numPumps number of pumps
     * @param numTills number of tills
     */
    private void simulate(Integer numOfTurns, Integer numPumps, Integer numTills, Double priceOfFuel){

        //build shop, filling station and the random that will be used throughout the application
        Shop shop = new Shop(numTills);
        FillingStation fillingStation = new FillingStation(numPumps);
        Random random = new Random(8);

        try {
            for (int i = 0; i < numOfTurns; i += SECONDS_PER_TICK) {
                simulateRound(fillingStation, shop, random, priceOfFuel);
            }
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        generateFile( getResults(shop, fillingStation));
    }

    private List<String> getResults(Shop shop, FillingStation fillingStation){
        List<String> results = new ArrayList<>();
        //just to make the results look cleaner make sure the right ending word is used
        String vehicle = checkIfPlural(fillingStation.getLeftOverCustomers(), "vehicle");
        String driver = checkIfPlural(shop.getShopFloor().size(), "driver");
        String tillDrivers = checkIfPlural(shop.getLeftOverCustomers(), "driver");

        //finally print out the money lost and gained.
        results.add("Results\n");
        results.add("Money lost: " + moneyLost + "\n");
        results.add("Money gained: " + moneyGained + "\n");
        results.add("Filling Station - Pumps: " + fillingStation.getLeftOverCustomers() + " " + vehicle + "\n");
        results.add("Shop - Tills: " + shop.getLeftOverCustomers() + " " + driver +"\n");
        results.add("Shop - floor: " + shop.getShopFloor().size() + " " + tillDrivers);

        return results;
    }

    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word = word + "s";
        }

        return word;
    }


    /**
     * Simulates the adding of drivers to the shop and the shop to the tills
     *
     * @param shop the shop that we are adding to
     * @param finishedAtPump the vehicles that have finished at the pump
     * @param priceOfFuel the price of fuel to calculate how much the driver will spend
     * @param random the random used to generate all of the values
     * @return returns a map of customers to the till they were at
     * @throws ServiceMachineAssigningException thrown when a driver can not be added to a till
     * @throws PumpNotFoundException returned when the correct pump can not be found.
     */
    public Shop runShoppers(Shop shop, Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws ServiceMachineAssigningException, PumpNotFoundException {

        List<Customer> finishedAtShop = shop.getDriversFinished();
        shop.removeDrivers(finishedAtShop);

        if(finishedAtPump.size() > 0) {
            List<List<Driver>> customers = shop.decideToGoToShop(finishedAtPump, random, priceOfFuel);
            setLostMoney(customers.get(0), finishedAtPump);

            Collection<Driver> nonShoppingCustomers = customers.get(0);

            if(customers.get(1).size() > 0) {
                shop.addToShopFloor(customers.get(1));
            }

            finishedAtShop.addAll(nonShoppingCustomers);
        }

        shop.addCustomerToMachine(finishedAtShop, priceOfFuel);

        return shop;
    }

    /**
     * Simulates a single round
     *
     * @param fillingStation the filling station
     * @param shop the shop
     * @param random the random that will be used to generate the vehicles
     * @throws ServiceMachineAssigningException error trying to add customer to a service machine
     */
    private void simulateRound(FillingStation fillingStation, Shop shop, Random random, Double priceOfFuel) throws ServiceMachineAssigningException, PumpNotFoundException {

        //Get the finished vehicles in the system
        Map<Integer, Customer> finishedAtPump = fillingStation.manageTransactions();

        //if there are finished vehicles set the chance of trucks spawning
        if(finishedAtPump.size() > 0) {
            setChanceOfTruck(finishedAtPump.values());
        }

        //create the vehicles for the round
        Collection<Customer> vehicles = generateVehicles(random);

        //if vehicles have been generated, add them to the queues
        if(vehicles.size() >  0){
            Collection<Customer> lostCustomers = fillingStation.addCustomerToMachine(vehicles, priceOfFuel);
            moneyLost += calculateLost(lostCustomers, priceOfFuel);
        }

        //get the finished shoppers in the system
        Map<Integer, Customer> finishedCustomers = shop.manageTransactions();

        //add the new shoppers to the correct locations
        shop = runShoppers(shop, finishedAtPump, priceOfFuel, random);

        //remove the finished shoppers from their tills
        for(Integer i : finishedCustomers.keySet()) {
            shop.getServiceMachines()[i].removeCustomer();
        }

        //remove the finished vehicles and shoppers from their pumps
        for (Customer customer : finishedCustomers.values()) {
            Driver driver = (Driver) customer;
            fillingStation.removeCustomerFromPump(driver.getPumpNumber());
            moneyGained += driver.getCurrentSpend();
        }
    }

    private Double calculateLost(Collection<Customer> lostCustomers, Double priceOfFuel){
        Double moneyLost = 0.0;
        for(Customer customer : lostCustomers){
            moneyLost += Math.round((customer.getSize() * priceOfFuel)*100/100);
        }
        return moneyLost;
    }

    /**
     * Get the lost money from customers not going to the shop
     *
     * @param customers all of the customers
     * @param finishedAtPump all of the vehicles and their pumps
     */
    private void setLostMoney(List<Driver> customers, Map<Integer, Customer> finishedAtPump) {
        //add the values lost to the overall lost
        if (customers.size() > 0) {
            for (Driver customer : customers) {
                for (int pump : finishedAtPump.keySet()) {
                    if (customer.getPumpNumber() == pump) {
                        Vehicle vehicle = (Vehicle) finishedAtPump.get(pump);
                        moneyLost += vehicle.getShopPurchase();
                    }
                }
            }
        }
    }

    /**
     * generates vehicles
     *
     * @param random random
     * @return list of generated vehicles
     */
    private List<Customer> generateVehicles(Random random){

        List<Customer> vehicles = new ArrayList<>();

        if(random.nextDouble() < p){
            vehicles.add(new Motorbike());
            System.out.println("A motorbike has arrived");
        }

        if (random.nextDouble() < p ){
            vehicles.add(new SmallCar(random));
            System.out.println("A small car has arrived");
        }

        if (random.nextDouble() < chanceOfTruck){
            vehicles.add(new Truck(random));
            System.out.println("A truck has arrived");
        }

        if(random.nextDouble() < q){
            vehicles.add(new FamilySedan(random));
            System.out.println("A family sedan has arrived");
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
            if(customer instanceof Truck){
                Truck truck = (Truck) customer;
                if(truck.getTimeInQueue() < truck.getMaxQueueTime()){
                    chanceOfTruck += ((chanceOfTruck / 100) * 5);
                } {
                    chanceOfTruck -= ((chanceOfTruck / 100) * 20) ;
                }
            }
        }
    }

    /**
     * Converts the time into seconds based on its identifier and returns the new value.
     * I chose this design because it is clear what is calculating what, and is easy to add new time formats
     *
     * @param time the amount of time the simulation will execute for.
     * @return time in seconds
     * @throws InvalidInputException time could not be converted.
     */
    private static Integer convertTimeIntoSeconds(String time, String identifier) throws InvalidInputException{

        Integer number;
        try {
            Double doubleNumber = Double.parseDouble(time);
            //Do a check on the identifier each time, if its a day you will need to do all of the calculations,
            //This is clearer as you can see what each calculation is doing to the number even though the added if statements add additional run time.
            if(identifier.equals("t")){
                doubleNumber *= 10;
            } else {
                if (identifier.equals("d")) {
                    doubleNumber *= 24;
                }

                if (identifier.equals("d") || identifier.equals("h")) {
                    doubleNumber *= 60;
                }

                if (identifier.equals("d") || identifier.equals("h") || identifier.equals("m")) {
                    doubleNumber *= 60;
                }
            }

            number = Integer.parseInt(Math.round(doubleNumber) + "");
        } catch (Exception e){
            throw new InvalidInputException(time);
        }

        return number;
    }

    /**
     * Writes results to a file
     * Prints result to screen as its writing.
     *
     * @param results list of all of the results.
     */
    private void generateFile(List<String> results){
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try{
            //create the file writer using the location store as a constant
            fileWriter = new FileWriter(RESULTS_DESTINATION_FILE);
            //create a buffered write with the file writer as an argument
            bufferedWriter = new BufferedWriter(fileWriter);
            //loop through the results list
            for(String message : results){
                //print out the line of the results
                System.out.println(message);
                //add the line to the file
                bufferedWriter.write(message);
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                //close the writers
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
                if(fileWriter != null){
                    fileWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}