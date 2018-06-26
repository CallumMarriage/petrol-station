package com.team2.petrolstation.model.customer;

import org.junit.Test;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.CHANCE_OF_SMALL_CAR_GOING_TO_SHOP;
import static com.team2.petrolstation.model.constant.VehicleConstants.MAX_QUEUE_TIME_SMALL_CAR;
import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_SMALL_CAR;
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
        Vehicle car = new Vehicle(random.nextInt(24 - 12 + 1) + 12, random.nextInt(10 -5 + 1) + 5, random.nextInt(9 - 7  + 1) + 7, CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR);

        Driver driver = new Driver(car.getMaxFuel() * 1.20, 1);
        assertTrue(driver.getCurrentSpend() > 0);

        assertTrue(driver.act(1));

        driver.setTimeInShop(car.getShopTime());
        driver.addToCurrentSpend(car.getShopPurchase());

        assertFalse(driver.act(4));

        assertTrue(driver.act(60));
    }
}
