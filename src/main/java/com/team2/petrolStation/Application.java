package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
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

    public static void main(String[] args){
        System.out.println("Hello There!");
        System.out.println("");
        System.out.println("General Kenobi");
    }

    public void simulate(Integer numOfTurns){
        Double moneyLost = 0.0;
        Double moneyGained = 0.0;
        Shop shop = new Shop();
        FillingStation fillingStation = new FillingStation();
        for(int i = 0; i < numOfTurns; i+=10 ){
            simulateRound(fillingStation, shop, moneyLost, moneyGained);
        }
    }

    public void simulateRound(FillingStation fillingStation, Shop shop, Double moneyLost, Double moneyGained){
        //create the vehicles for the round
        Collection<Customer> vehicles = generateVehicles();

        //put all of the vehicles into the queues for the pumps and refuel the ones at the front of each queue
        Map<Integer, Customer> finishedAtPump = fillingStation.manageTransactions(vehicles);

        //alter the change of trucks arriving based on the time the trucks took to finish refuelling
        setChanceOfTruck(finishedAtPump.values());

        //check if drivers are going to the shop, split the hash map to those going to the shop floor and those going directly to the tills.
        List<List<Driver>> customers = shop.decideToGoToShop(finishedAtPump);

        //add the values lost to the overall lost
        Collection<Driver> nonShoppingCustomers = customers.get(0);

        for(Driver customer : nonShoppingCustomers){
            moneyLost += customer.getMaximumSpend();
        }
        //add customers to the shop floor, simulate time on the shop and floor and return finished drivers. All drivers that did not shop should add the lost amount to the lost money tally.
        List<Driver> finishedAtShop = shop.addToShopFloor(customers.get(1));

        //add the drivers who have left the shop floor to the queues for the tills and do transactions
        finishedAtShop.addAll(nonShoppingCustomers);
        Map<Integer, Customer> finishedCustomers = shop.manageTransactions(finishedAtShop);

        //add the money from all the finished customers to the overall amount.
        for(Customer customer : finishedCustomers.values() ){
            Driver driver = (Driver) customer;
            moneyGained += driver.getCurrentSpend();
        }
    }

    public Collection<Customer> generateVehicles(){

    }

    public void setChanceOfTruck(Collection<Customer> customers){
        for(Customer customer : customers){
            if(customer instanceof Truck){
                Vehicle vehicle = (Vehicle) customer;
                if(vehicle.getTimeInQueue() < 5){
                    chanceOfTruck += 0.02;
                } {
                    chanceOfTruck -= 0.03;
                }
            }
        }
    }

}
