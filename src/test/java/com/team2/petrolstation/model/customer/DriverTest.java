package com.team2.petrolstation.model.customer;

import com.team2.petrolstation.model.customer.vehicle.SmallCar;
import com.team2.petrolstation.model.customer.vehicle.Vehicle;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * @author callummarriage
 */
public class DriverTest {

    @Test
    public void driverActTest(){
        Random random = new Random(1);
        Vehicle car = new SmallCar(random);

        Driver driver = new Driver(car.getMaxFuel() * 1.20, 1);
        assertTrue(driver.getCurrentSpend() > 0);

        assertTrue(driver.act(1));

        driver.setTimeInShop(car.getShopTime());
        driver.addToCurrentSpend(car.getShopPurchase());

        assertFalse(driver.act(4));

        assertTrue(driver.act(60));
    }
}