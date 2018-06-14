package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.InvalidInputException;
import com.team2.petrolStation.model.exceptions.PumpNotFoundException;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;
import com.team2.petrolStation.model.views.Simulator;
import com.team2.petrolStation.model.views.Text;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.SECONDS_PER_TICK;

/**
 * Application class allows for the running of simulations that generate vehicles and then refuels them and sends them to a shop to shop and or purchase their fuel.
 * It also keeps track of the amount of money gained and lost during the simulation.
 * Generates the front end of which allows the user to input values that affect the running of the simulation and displays the results.
 *
 * @author callummarriage canershefik
 */
public class Application implements Simulator{

    private Double chanceOfTruck;
    private Double moneyGained;
    private Double moneyLost;
    private static Double p;
    private static Double q;

    public static void main(String[] args){
        Double chanceOfTrucks = 0.02;
        Application application = new Application(chanceOfTrucks);

        //replace this with gui values
        Integer numOfTurns;
        Integer numPumps = 1;
        Integer numTills = 1;
        Double priceOfFuel = 1.2;
        p = 0.01;
        q = 0.02;

        String time = "1";
        String identifiers = "d";

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

        Text textView = new Text(this);

        textView.printFinalResults(shop, fillingStation, moneyLost, moneyGained);
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

    /**
     * Calculates the lost money from vehicles not finding a pump
     *
     * @param lostCustomers customers
     * @param priceOfFuel price of fuel
     * @return amount of money lost
     */
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
        Double randomNum = random.nextDouble();

        try {
            if (randomNum > p && randomNum <= (2 * p)) {
                vehicles.add(new Motorbike());
                System.out.println("A motorbike has arrived");
                TimeUnit.MILLISECONDS.sleep(100);            }

            if (randomNum <= p) {
                vehicles.add(new SmallCar(random));
                System.out.println("A small car has arrived");
                TimeUnit.MILLISECONDS.sleep(100);            }

            if (randomNum > ((2 * p) + q) && randomNum <= ((2 * p) + q) + chanceOfTruck) {
                vehicles.add(new Truck(random));
                System.out.println("A truck has arrived");
                TimeUnit.MILLISECONDS.sleep(100);            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                vehicles.add(new FamilySedan(random));
                System.out.println("A family sedan has arrived");
                TimeUnit.MILLISECONDS.sleep(100);            }
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
                switch (identifier){
                    //year
                    case ("y"): doubleNumber *= 31536000;
                    break;
                    //week
                    case ("w"): doubleNumber *= 604800;
                    break;
                    //day
                    case ("d"): doubleNumber *= 86400;
                    break;
                    //hour
                    case ("h"): doubleNumber *= 3600;
                    break;
                    //minute
                    case ("m"): doubleNumber *= 60;
                    break;
                    //tick
                    case ("t"): doubleNumber *=10;
                    default: break;
                }
            }

            number = Integer.parseInt(Math.round(doubleNumber) + "");
        } catch (Exception e){
            throw new InvalidInputException(time);
        }

        return number;
    }
}