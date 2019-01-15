package com.team2.petrolstation.model;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import com.team2.petrolstation.model.facility.FillingStation;
import com.team2.petrolstation.model.facility.Shop;

import java.util.Map;
import java.util.Random;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.START;

/**
 * Created by callummarriage on 15/01/2019.
 */
public class PetrolStation {

    private FillingStation fillingStation;
    private Shop shop;

    public PetrolStation(int numTills, int numPumps){
        this.shop = new Shop(numTills);
        this.fillingStation = new FillingStation(numPumps);
    }

    /**
     * Manages the transactions for the filling station, then sets the chance of trucks arriving based on this information.
     *
     * @return finished drivers with the pump number they have left behind
     */
    public Map<Integer, Customer> runFillingStation() {
        //Get the finished vehicles in the system
        return this.fillingStation.manageTransactions();
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
    public Map<Integer, Customer> runShop(Map<Integer, Customer> finishedAtPump, Double priceOfFuel, Random random) throws Exception {
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
    public String removeCustomers(Map<Integer, Customer> finishedCustomers) throws PumpNotFoundException {
        StringBuilder sb = new StringBuilder();
        //remove the finished vehicles and shoppers from their pumps
        for (Map.Entry<Integer, Customer> customerEntry : finishedCustomers.entrySet()) {
            this.fillingStation.removeCustomerFromPump(((Driver) customerEntry.getValue()).getPumpNumber());
            sb.append(this.shop.removeDriversFromShop(customerEntry));
        }
        return sb.toString();
    }

    public String addVehicleToPetrolStation(Vehicle vehicle, Double priceOfFuel){
        return this.fillingStation.addVehicleToPetrolStation(vehicle, priceOfFuel);
    }

    public FillingStation getFillingStation() {
        return fillingStation;
    }

    public Shop getShop() {
        return shop;
    }

    /**
     * Get Results based on performance
     *
     * @return return all of the contents
     */
    public String getResults(){

        //just to make the results look cleaner make sure the right ending word is used
        String vehicle = checkIfPlural(fillingStation.getLeftOverCustomers(), "vehicle");
        String driver = checkIfPlural(shop.getShopFloor().size(), "driver");
        String tillDrivers = checkIfPlural(shop.getLeftOverCustomers(), "driver");

        //finally print out the money lost and gained.
        return START + "\n* Finances *\nMoney lost from Filling Station: $" + fillingStation.getMoneyLost() +
                "\nMoney lost from Shop: $" + shop.getMoneyLost() +
                "\nMoney gained: $" + shop.getMoneyGained() +
                "\n\n* Left over customers *\nFilling Station - Pumps: " + fillingStation.getLeftOverCustomers() + " " + vehicle +
                "\nShop - Tills: "+ shop.getLeftOverCustomers() + " " + driver +
                "\nShop - floor: " + shop.getShopFloor().size() + " " + tillDrivers;
    }

    public Double getMoneyLost(){
        return this.fillingStation.getMoneyLost() + this.getShop().getMoneyLost();
    }

    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word += "s";
        }

        return word;
    }
}
