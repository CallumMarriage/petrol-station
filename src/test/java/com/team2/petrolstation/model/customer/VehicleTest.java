package com.team2.petrolstation.model.customer;

import org.junit.Test;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.CHANCE_OF_SMALL_CAR_GOING_TO_SHOP;
import static com.team2.petrolstation.model.constant.VehicleConstants.MAX_QUEUE_TIME_SMALL_CAR;
import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_SMALL_CAR;
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

        Vehicle smallCar =new Vehicle(random.nextInt(24 - 12 + 1) + 12, random.nextInt(10 -5 + 1) + 5, random.nextInt(9 - 7  + 1) + 7, CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR);

        assertFalse(smallCar.act(1));

        assertTrue(smallCar.act(20));
    }

    @Test
    public void testSmallCarFuelTank(){

        Random random = new Random(1);

        Vehicle smallCar = new Vehicle(random.nextInt(24 - 12 + 1) + 12, random.nextInt(10 -5 + 1) + 5, random.nextInt(9 - 7  + 1) + 7, CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR);

        System.out.println(smallCar.getMaxFuel());

        assertTrue(smallCar.getMaxFuel() >= 7 && smallCar.getMaxFuel() <= 9);
    }

}
