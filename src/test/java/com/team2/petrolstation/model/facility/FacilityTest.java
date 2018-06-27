package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Customer;
import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.*;
import static com.team2.petrolstation.model.constant.VehicleConstants.MAX_QUEUE_TIME_TRUCK;
import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_MOTORBIKE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests for Facility class
 *
 * @author callummarriage
 */
public class FacilityTest {


    @Test
    public void testAddVehicleToPump(){
        Random random = new Random(3);
        Vehicle truck = new Vehicle((random.nextInt(36 -24 +1  ) +24), (random.nextInt(20 - 15 + 1) + 15), (random.nextInt(40 - 30 + 1) + 30), CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
        Vehicle car = new Vehicle((random.nextInt(24 - 12 + 1) + 12), (random.nextInt(10 -5 + 1) + 5), (random.nextInt(9 - 7  + 1) + 7), CHANCE_OF_SMALL_CAR_GOING_TO_SHOP, SIZE_OF_SMALL_CAR, MAX_QUEUE_TIME_SMALL_CAR);
        Vehicle motorbike = new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0);
        Vehicle familySedan = new Vehicle((random.nextInt(30 - 12 + 1) + 12), random.nextInt(16 - 8 + 1) + 8, random.nextInt(18) + 12, CHANCE_OF_FAMILY_SEDAN_GOING_TO_SHOP, SIZE_OF_FAMILY_SEDAN, MAX_QUEUE_TIME_SMALL_CAR);

        FillingStation facility = new FillingStation(2);

        Collection<Customer> vehicles = new ArrayList<Customer>();
        vehicles.add(truck);
        vehicles.add(car);

        assertEquals(0, facility.getServiceMachines()[0].getCustomersInQueue().size());
        assertEquals(0, facility.getServiceMachines()[1].getCustomersInQueue().size());

        try {
            facility.addCustomerToMachine(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, facility.getServiceMachines()[0].getCustomersInQueue().size());
        assertEquals(1, facility.getServiceMachines()[1].getCustomersInQueue().size());

        assertEquals(truck, facility.getServiceMachines()[0].getCustomersInQueue().peek());
        assertEquals(car, facility.getServiceMachines()[1].getCustomersInQueue().peek());

        vehicles.clear();
        vehicles.add(motorbike);
        vehicles.add(familySedan);

        try{
            facility.addCustomerToMachine(vehicles);
        } catch (Exception e){
            e.printStackTrace();
        }

        assertEquals(1, facility.getServiceMachines()[0].getCustomersInQueue().size());
        assertEquals(3, facility.getServiceMachines()[1].getCustomersInQueue().size());

    }

    @Test
    public void findBestServiceMachine(){
        Random random = new Random();
        Vehicle truck = new Vehicle((random.nextInt(36 -24 +1  ) +24), (random.nextInt(20 - 15 + 1) + 15), (random.nextInt(40 - 30 + 1) + 30), CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
        Vehicle truck2 = new Vehicle((random.nextInt(36 -24 +1  ) +24), (random.nextInt(20 - 15 + 1) + 15), (random.nextInt(40 - 30 + 1) + 30), CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
        Vehicle truck3 = new Vehicle((random.nextInt(36 -24 +1  ) +24), (random.nextInt(20 - 15 + 1) + 15), (random.nextInt(40 - 30 + 1) + 30), CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
        Vehicle truck4 = new Vehicle((random.nextInt(36 -24 +1  ) +24), (random.nextInt(20 - 15 + 1) + 15), (random.nextInt(40 - 30 + 1) + 30), CHANCE_OF_TRUCK_GOING_TO_SHOP, SIZE_OF_TRUCK, MAX_QUEUE_TIME_TRUCK);
        Vehicle motorbike = new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0);

        FillingStation fillingStation = new FillingStation(3);
        try {
            assertTrue(0 == fillingStation.findBestMachine(truck));
            fillingStation.addCustomerToBestMachine(0, truck);
            assertTrue(1 == fillingStation.findBestMachine(truck2));
            fillingStation.addCustomerToBestMachine(1, truck2);
            assertTrue(2 == fillingStation.findBestMachine(truck3));
            fillingStation.addCustomerToBestMachine(2, truck3);
            assertTrue(0 == fillingStation.findBestMachine(motorbike));
            assertTrue(-1 == fillingStation.findBestMachine(truck4));
            fillingStation.removeCustomerFromPump(0);
            assertTrue(0 == fillingStation.findBestMachine(truck));
        } catch (PumpNotFoundException e) {
            e.printStackTrace();
        }
    }
}
