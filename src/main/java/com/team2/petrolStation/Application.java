package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.InvalidInputException;
import com.team2.petrolStation.model.exceptions.PumpNotFoundException;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;

import java.util.*;

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
        Integer numPumps = 6;
        Integer numTills = 2;

        try {
            numOfTurns = convertTimeIntoSeconds("5h");

        } catch(InvalidInputException e){
            e.printStackTrace();
            return;
        }

        System.out.println("Welcome to the simulation\nThe duration will be " + numOfTurns + " seconds or " + (numOfTurns / 6) + " ticks \n");

        //run the simulation using the inputed values
        application.simulate(numOfTurns, numPumps, numTills, p, q);
    }

    public Application(Double chanceOfTrucks){
        moneyLost = 0.0;
        moneyGained = 0.0;
        this.chanceOfTruck = chanceOfTrucks;
        p = 0.01;
        q = 0.02;
    }

    /**
     * Simulates the petrol station and calls simulateRound for as long as chosen.
     *
     * @param numOfTurns number of turns in seconds
     * @param numPumps number of pumps
     * @param numTills number of tills
     * @param p the chance of a small car of motorbike spawning
     * @param q the chance of a truck spawning
     */
    private void simulate(Integer numOfTurns, Integer numPumps, Integer numTills, Double p, Double q){

        //build shop, filling station and the random that will be used throughout the application
        Shop shop = new Shop(numTills);
        FillingStation fillingStation = new FillingStation(numPumps);
        Random random = new Random(9);

        try {
            for (int i = 0; i < numOfTurns; i += 10) {
                simulateRound(fillingStation, shop, random, p, q);
            }
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        //finally print out the money lost and gained.
        System.out.println("Money lost: " + moneyLost);
        System.out.println("Money gained: " + moneyGained);
        System.out.println("\n");

        System.out.println("Filling Station Pumps:");
        fillingStation.printLeftOverCustomers();
        System.out.println("Shop Tills:");
        shop.printLeftOverCustomers();


        System.out.println("Shop floor:");
        System.out.println("Number of customers on the Shop floor " + shop.getShopFloor().size() + ".");
    }

    /**
     * Simulates a single round
     *
     * @param fillingStation the filling station
     * @param shop the shop
     * @param random the random that will be used to generate the vehicles
     * @param p the chance of a small car of motorbike spawning
     * @param q the chance of a truck spawning
     * @throws ServiceMachineAssigningException error trying to add customer to a service machine
     */
    private void simulateRound(FillingStation fillingStation, Shop shop, Random random, Double p, Double q) throws ServiceMachineAssigningException, PumpNotFoundException {

        //create the vehicles for the round
        Collection<Customer> vehicles = generateVehicles(random, p, q);

        if(vehicles.size() == 0){
            return;
        }

        //put all of the vehicles into the queues for the pumps and refuel the ones at the front of each queue
        Map<Integer, Customer> finishedAtPump = fillingStation.manageTransactions();

        moneyLost += fillingStation.addCustomerToMachine(vehicles);

        List<List<Driver>> customers = new ArrayList<List<Driver>>();

        //alter the change of trucks arriving based on the time the trucks took to finish refuelling
        if(finishedAtPump.values().size() == 0){
            return;
        }

        setChanceOfTruck(finishedAtPump.values());

        shop.decideToGoToShop(finishedAtPump, random);
        customers = shop.decideToGoToShop(finishedAtPump, random);

        Collection<Driver> nonShoppingCustomers = new ArrayList<>();
        //add the values lost to the overall lost
        if(customers.get(0).size() >0) {
             nonShoppingCustomers = customers.get(0);

            for (Driver customer : nonShoppingCustomers) {
                moneyLost += customer.getMaximumSpend();
            }
        }

        List<Customer> finishedAtShop = new ArrayList<>();
        //add customers to the shop floor, simulate time on the shop and floor and return finished drivers. All drivers that did not shop should add the lost amount to the lost money tally.
        if(customers.get(1).size() > 0) {
            shop.addToShopFloor(customers.get(1));
             finishedAtShop = shop.getDriversFinished();
             shop.removeDrivers(finishedAtShop);
            //add the drivers who have left the shop floor to the queues for the tills and do transactions
        }

        finishedAtShop.addAll(nonShoppingCustomers);

        Map<Integer, Customer> finishedCustomers = shop.manageTransactions();

        shop.addCustomerToMachine(finishedAtShop);

        //add the money from all the finished customers to the overall amount.
        for (Customer customer : finishedCustomers.values()) {
            Driver driver = (Driver) customer;
            fillingStation.removeCustomerFromPump(driver.getPumpNumber());
            moneyGained += driver.getCurrentSpend();
        }
    }

    /**
     * generates vehicles
     *
     * @param random random
     * @param p the chance of a small car of motorbike spawning
     * @param q the chance of a truck spawning
     * @return list of generated vehicles
     */
    private List<Customer> generateVehicles(Random random,Double p,Double q){
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

    public void setChanceOfTruck(Collection<Customer> customers){
        for(Customer customer : customers){
            if(customer instanceof Truck){
                Vehicle vehicle = (Vehicle) customer;
                if(vehicle.getTimeInQueue() < vehicle.getMaxQueueTime()){
                    chanceOfTruck += ((chanceOfTruck / 100) * 5);
                } {
                    chanceOfTruck -= ((chanceOfTruck / 100) * 20) ;
                }
            }
        }
    }

    /**
     * Converts the time into seconds based on its identifier and returns the new value.
     *
     * @param time the amount of time the simulation will execute for.
     * @return time in seconds
     * @throws InvalidInputException time could not be converted.
     */
    public static Integer convertTimeIntoSeconds(String time) throws InvalidInputException{

        String[] identifiers = {"h","m", "s"};

        Boolean isIdentifier = false;
        int i = 0;
        String identifier = "";
        String[] timeSplit = null;
        //split the time
        while(!isIdentifier || i == identifiers.length){
            identifier = identifiers[i];
            timeSplit = time.split("((?<=" +  identifier + ")|(?=" + identifier + "))");
            if(timeSplit.length > 1){
                //if there is an identifier return true
                isIdentifier = true;
            }
            i++;
        }

        Integer number = 0;

        try{
            number = Integer.parseInt(timeSplit[0]);
            //if there is no identifier assume that the value is in seconds and return
            if(timeSplit.length == 1){
                return number;
            }
            identifier = timeSplit[1];
            if(identifier.equals("m")){
                number *= 60;
            } else if(identifier.equals("h")){
                number *= (60 * 60);
            }
        } catch (Exception e){
            throw new InvalidInputException(time);
        }
        return number;
    }
}
