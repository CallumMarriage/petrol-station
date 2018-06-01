package com.team2.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Motorbike;
import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.facility.Shop;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author callummarriage
 */
public class ShopTest {

    @Test
    public void testManageTransactionsForShop(){
        Shop shop = new Shop(1);

        Driver driver = new Driver(5 * 1.20, 1);
        shop.addCustomerToBestMachine(0, driver);

        Map<Integer, Customer> driverWithPump = shop.manageTransactions();

        assertEquals(0 , driverWithPump.keySet().toArray()[0]);

    }

    @Test
    public void testDecideToGoToShop(){

        Random random = new Random(1);
        Map<Integer, Customer> vehicles = new HashMap<>();
        Motorbike motorbike = new Motorbike();
        vehicles.put(0, motorbike);
        Shop shop = new Shop(1);

        List<List<Driver>> allCustomers = shop.decideToGoToShop(vehicles, random);

        assertTrue(0 == allCustomers.get(0).get(0).getPumpNumber());

        Truck truck = new Truck(random);

        for(int i = 0; i < 40; i++){
            truck.act(1);
        }

        SmallCar smallCar = new SmallCar(random);
        SmallCar smallCar2 = new SmallCar(random);
        SmallCar smallCar3 = new SmallCar(random);
        SmallCar smallCar4 = new SmallCar(random);

        vehicles.clear();

        vehicles.put(1, smallCar);
        vehicles.put(2, smallCar2);
        vehicles.put(3, smallCar3);
        vehicles.put(4, smallCar4);

        allCustomers = shop.decideToGoToShop(vehicles, random);

        assertTrue(0 == allCustomers.get(0).get(0).getPumpNumber());

    }

    @Test
    public void testAddCustomerToTill(){

        Shop facility = new Shop(1);
        for(int i = 0; i < 5; i++){
            facility.addCustomerToBestMachine(0, new Driver(1 * 1.20, 1));
        }

        assertEquals(5, facility.getServiceMachines()[0].getCustomersInQueue().size());
    }

}
