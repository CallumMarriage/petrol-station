package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Driver;
import com.team2.petrolstation.model.customer.Vehicle;
import org.junit.Test;

import java.util.*;

import static com.team2.petrolstation.model.constant.VehicleConstants.CHANCE_OF_SMALL_CAR_GOING_TO_SHOP;
import static com.team2.petrolstation.model.constant.VehicleConstants.MAX_QUEUE_TIME_SMALL_CAR;
import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_SMALL_CAR;
import static org.junit.Assert.assertEquals;

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
    public void testAddCustomerToTill(){

        Shop facility = new Shop(1);
        for(int i = 0; i < 5; i++){
            facility.addCustomerToBestMachine(0, new Driver(1 * 1.20, 1));
        }

        assertEquals(5, facility.getServiceMachines()[0].getCustomersInQueue().size());
    }

}
