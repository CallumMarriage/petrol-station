package com.team2.model.customer;

import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
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

        Driver driver = new Driver(car.getMaxFuel(), 1);
        assertTrue(driver.getCurrentSpend() > 0);

        assertTrue(driver.act(1));

        driver.setTimeInShop(car.getShopTime());
        driver.setCurrentSpend(car.getShopPurchase());
        driver.setMaximumSpend(car.getShopPurchase());

        assertFalse(driver.act(4));

        assertTrue(driver.act(60));
    }
}
