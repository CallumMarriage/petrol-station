package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.InvalidInputException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;

import java.util.*;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.*;

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

        Integer numOfTurns = 0;
        Integer numPumps = 4;
        Integer numTills = 2;
        try {
            numOfTurns = convertTimeIntoSeconds("5h");

        } catch(InvalidInputException e){
            e.printStackTrace();
            return;
        }
        System.out.println(numOfTurns);

        /*
        if(args[0] != null && args[1] != null && args[2] != null){
            numOfTurns = Integer.parseInt(args[0]);
            numPumps = Integer.parseInt(args[1]);
            numTills = Integer.parseInt(args[2]);
        } else {
            System.out.println("Incorrect arguments");
            return;
        }*/

        application.simulate(numOfTurns, numPumps, numTills, p, q);
    }
    public Application(Double chanceOfTrucks){
        moneyLost = 0.0;
        moneyGained = 0.0;
        this.chanceOfTruck = chanceOfTrucks;
        p = 0.01;
        q = 0.02;
    }

    public void simulate(Integer numOfTurns, Integer numPumps, Integer numTills, Double p, Double q){

        Shop shop = new Shop(numTills);
        FillingStation fillingStation = new FillingStation(numPumps);
        Random random = new Random(8);

        for (int i = 0; i < numOfTurns; i += 10) {
            simulateRound(fillingStation, shop, random, p, q);
        }


        System.out.println("Money lost: " + moneyLost);
        System.out.println("Money gained: " + moneyGained);
    }

    public void simulateRound(FillingStation fillingStation, Shop shop, Random random, Double p, Double q){
        //create the vehicles for the round
        Collection<Customer> vehicles = generateVehicles(random, p, q);

        if(vehicles.size() == 0){
            return;
        }
        moneyLost += fillingStation.addCustomerToMachine(vehicles);
        //put all of the vehicles into the queues for the pumps and refuel the ones at the front of each queue
        Map<Integer, Customer> finishedAtPump = fillingStation.manageTransactions();

        List<List<Driver>> customers = new ArrayList<List<Driver>>();

        //alter the change of trucks arriving based on the time the trucks took to finish refuelling
        if(finishedAtPump.values().size() == 0){
            return;
        }

        setChanceOfTruck(finishedAtPump.values());
        customers = shop.decideToGoToShop(finishedAtPump);


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
            //add the drivers who have left the shop floor to the queues for the tills and do transactions
        }

        finishedAtShop.addAll(nonShoppingCustomers);

        shop.addCustomerToMachine(finishedAtShop);

        Map<Integer, Customer> finishedCustomers = shop.manageTransactions();
        //add the money from all the finished customers to the overall amount.
        for (Customer customer : finishedCustomers.values()) {
            Driver driver = (Driver) customer;
            moneyGained += driver.getCurrentSpend();
        }
    }

    public List<Customer> generateVehicles(Random random,Double p,Double q){
        List<Customer> vehicles = new ArrayList<>();
        if(random.nextDouble() < p){
            vehicles.add(new Motorbike());
            System.out.println("A motorbike has arrived");
        }
        if (random.nextDouble() < p ){
            vehicles.add(new SmallCar());
            System.out.println("A small car has arrived");
        }
        if (random.nextDouble() < chanceOfTruck){
            vehicles.add(new Truck());
            System.out.println("A truck has arrived");
        }

        if(random.nextDouble() < q){
            vehicles.add(new FamilySedan());
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

    public static Integer convertTimeIntoSeconds(String time) throws InvalidInputException{

        String[] identifiers = {"h","m", "s"};

        Boolean isIdentifier = false;
        int i = 0;
        String identifier = "";
        String[] timeSplit = null;
        while(!isIdentifier){
            identifier = identifiers[i];
            timeSplit = time.split("((?<=" +  identifier + ")|(?=" + identifier + "))");
            if(timeSplit.length > 1){
                isIdentifier = true;
            }
            i++;

        }

        Integer number = 0;

        try{
            number = Integer.parseInt(timeSplit[0]);
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
