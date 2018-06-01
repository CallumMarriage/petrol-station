package com.team2.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.facility.Shop;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author callummarriage
 */
public class ShopTest {


    @Test
    public void testManageTransactionsForShop(){
        Shop shop = new Shop(1);

        Driver driver = new Driver(1 * 1.20, 1);
        shop.addCustomerToBestMachine(0, driver);

        Map<Integer, Customer> driverWithPump = shop.manageTransactions();

        assertEquals(0 , driverWithPump.keySet().toArray()[0]);

    }

    @Test
    public void testDecideToGoToShop(){
        Shop shop = new Shop(1);

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
