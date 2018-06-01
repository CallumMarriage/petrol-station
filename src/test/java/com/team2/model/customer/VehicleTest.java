package com.team2.model.customer;

import com.team2.petrolStation.model.customer.vehicle.SmallCar;
import org.junit.Test;

import java.util.Random;

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

        Random random = new Random(1);

        SmallCar smallCar = new SmallCar(random);

        assertFalse(smallCar.act(1));

        assertTrue(smallCar.act(20));
    }

    @Test
    public void testSmallCarFuelTank(){

        Random random = new Random(1);

        SmallCar smallCar = new SmallCar(random);

        System.out.println(smallCar.getMaxFuel());

        assertTrue(smallCar.getMaxFuel() >= 7 && smallCar.getMaxFuel() <= 9);
    }

}
