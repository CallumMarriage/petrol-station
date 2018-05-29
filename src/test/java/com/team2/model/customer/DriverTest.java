package com.team2.model.customer;

import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * @author callummarriage
 */
public class DriverTest {

    @Test
    public void driverActTest(){
        Vehicle car = new SmallCar();

        Driver driver = new Driver(car.getMaxFuel());
        assertTrue(driver.getCurrentSpend() > 0);

        assertTrue(driver.decideToGoToShop(car));

        driver.setTimeInShop(car.getShopTime());
        driver.setCurrentSpend(car.getShopPurchase());
        driver.setMaximumSpend(car.getShopPurchase());

        assertFalse(driver.act(4));

        assertTrue(driver.act(60));
    }
}
