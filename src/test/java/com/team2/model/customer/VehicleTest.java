package com.team2.model.customer;

import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the refueling of an individual vehicle
 *
 * @author callummarriage canershefik
 */
public class VehicleTest {


    @Test
    public void testActVehicle(){

        SmallCar smallCar = new SmallCar();

        assertFalse(smallCar.act(1));

        assertTrue(smallCar.act(20));
    }

    @Test
    public void testSmallCarFuelTank(){

        SmallCar smallCar = new SmallCar();

        System.out.println(smallCar.getMaxFuel());

        assertTrue(smallCar.getMaxFuel() >= 7 && smallCar.getMaxFuel() <= 9);
    }

}
