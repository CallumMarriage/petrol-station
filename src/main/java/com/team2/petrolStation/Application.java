package com.team2.petrolStation;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.PumpNotFoundException;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;
import com.team2.petrolStation.model.views.Simulator;
import com.team2.petrolStation.model.views.ApplicationView;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.SECONDS_PER_TICK;
import static com.team2.petrolStation.model.constants.PetrolStationConstants.SLEEP_TIME;

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
    private Double p;
    private Double q;
    private ApplicationView applicationView;
    private FillingStation fillingStation;
    private Shop shop;

    public static void main(String[] args){
        Double chanceOfTrucks = 0.02;
        new Application(chanceOfTrucks);
    }

    public Application(Double chanceOfTrucks){
        moneyLost = 0.0;
        moneyGained = 0.0;
        this.chanceOfTruck = chanceOfTrucks;
        applicationView = new ApplicationView(this);
        applicationView.start();
    }


    /**
     * Simulates the petrol station and calls simulateRound for as long as chosen.
     *
     * @param numOfTurns number of turns in seconds
     * @param numPumps number of pumps
     * @param numTills number of tills
     */
    public void simulate(Integer numOfTurns, Integer numPumps, Integer numTills, Double priceOfFuel, Double p, Double q){

        this.p = p;
        this.q = q;

        //build shop, filling station and the random that will be used throughout the application
        this.shop = new Shop(numTills);
        this.fillingStation = new FillingStation(numPumps);
        Random random = new Random(8);

        try {
            for (int i = 0; i < numOfTurns; i += SECONDS_PER_TICK) {
                simulateRound( random, priceOfFuel);
            }
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        applicationView.printFinalResults(shop, fillingStation, moneyLost, moneyGained);
    }


    /**
     * Simulates a single round
     *
     * @param random the random that will be used to generate the vehicles
     * @throws ServiceMachineAssigningException error trying to add customer to a service machine
     */
    private void simulateRound( Random random, Double priceOfFuel) throws ServiceMachineAssigningException, PumpNotFoundException {

        Map<Integer, Customer> finishedAtPump = runFillingStation();

        //create the vehicles for the round and assign them to a pump
        assignVehicles(generateVehicles(random), priceOfFuel);

        Map<Integer, Customer> finishedCustomers = runShop( finishedAtPump, priceOfFuel, random);

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
     * Simulates the adding of drivers to the shop and the shop to the tills
     *
     * @param finishedAtPump the vehicles that have finished at the pump
     * @param priceOfFuel the price of fuel to calculate how much the driver will spend
     * @param random the random used to generate all of the values
     * @throws ServiceMachineAssigningException thrown when a driver can not be added to a till
     * @throws PumpNotFoundException returned when the correct pump can not be found.
     */
    private void assignShoppers( Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws ServiceMachineAssigningException, PumpNotFoundException {

        //get drivers who have finished loitering in the shop
        List<Customer> finishedAtShop = shop.getDriversFinished();
        //remove the drivers who are finished from the shopfloor
        shop.removeDrivers(finishedAtShop);

        if(finishedAtPump.size() > 0) {
            //drivers decide if they want to go on the shop floor
            List<List<Driver>> customers = shop.decideToGoToShop(finishedAtPump, random, priceOfFuel);

            //add all of the money lost from all the drivers who dont want to loiter
            setLostMoney(customers.get(0), finishedAtPump);

            //add all of the people who dont want to, to the list of finished drivers
            finishedAtShop.addAll(customers.get(0));

            //add the drivers who do want to go to the shop floor to it
            shop.addToShopFloor(customers.get(1));

        }

        //add the drivers who are finished with the shop floor to tills.
        shop.addCustomerToMachine(finishedAtShop, priceOfFuel);
    }

    /**
     * Generates vehicles and assigns the new vehicles to the best pump
     *
     * @param priceOfFuel price of fuel
     * @param vehicles all the new vehicles
     * @throws ServiceMachineAssigningException assign vehicles
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private void assignVehicles( Collection<Customer> vehicles, Double priceOfFuel) throws ServiceMachineAssigningException, PumpNotFoundException {

        //if vehicles have been generated, add them to the queues
        if(vehicles.size() >  0){
            Collection<Customer> lostCustomers = fillingStation.addCustomerToMachine(vehicles, priceOfFuel);
            moneyLost += calculateLostPerVehicle(lostCustomers, priceOfFuel);
        }
    }

    /**
     * Manages the transactions for the filling station, then sets the chance of trucks arriving based on this information.
     *
     * @return finished drivers with the pump number they have left behind
     */
    private Map<Integer, Customer> runFillingStation() {
        //Get the finished vehicles in the system
        Map<Integer, Customer> finishedAtPump = fillingStation.manageTransactions();

        //if there are finished vehicles set the chance of trucks spawning
        if(finishedAtPump.size() > 0) {
            setChanceOfTruck(finishedAtPump.values());
        }

        return finishedAtPump;
    }

    /**
     * gets finished drivers from the shop and assigned new drivers to their relevant places in the shop (the queue for the till or the shop floor)
     * @param finishedAtPump drivers and their service machines
     * @param priceOfFuel price of fuel
     * @param random random
     * @return drivers who are finished and the pump where their vehicle is
     * @throws ServiceMachineAssigningException Could not add a customer to the servicemachine that the algorithm suggested
     * @throws PumpNotFoundException could not find a valid service machine, this is only thrown if for drivers
     */
    private Map<Integer, Customer> runShop(Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws ServiceMachineAssigningException, PumpNotFoundException {
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
    private Double calculateLostPerVehicle(Collection<Customer> lostCustomers, Double priceOfFuel){
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

        for (Driver customer : customers) {
            for (int pump : finishedAtPump.keySet()) {
                if (customer.getPumpNumber() == pump) {
                    moneyLost += ((Vehicle) finishedAtPump.get(pump)).getShopPurchase();
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
                applicationView.updateScreen("A motorbike has arrived");
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);            }

            if (randomNum <= p) {
                vehicles.add(new SmallCar(random));
                applicationView.updateScreen("A small car has arrived");
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);            }

            if (randomNum > ((2 * p) + q) && randomNum <= ((2 * p) + q) + chanceOfTruck) {
                vehicles.add(new Truck(random));
                applicationView.updateScreen("A truck has arrived");
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);            }

            if (randomNum > (2 * p) && randomNum <= ((2 * p) + q)) {
                vehicles.add(new FamilySedan(random));
                applicationView.updateScreen("A family sedan has arrived");
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);            }
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
}