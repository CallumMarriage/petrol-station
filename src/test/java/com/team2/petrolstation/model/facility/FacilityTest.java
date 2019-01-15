package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.model.exception.PumpNotFoundException;
import com.team2.petrolstation.util.VehicleGeneratorUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.*;
import static com.team2.petrolstation.model.constant.VehicleConstants.MAX_QUEUE_TIME_TRUCK;
import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_MOTORBIKE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Tests for Facility class
 *
 * @author callummarriage
 */
public class FacilityTest {

    Vehicle truck;
    Vehicle car;
    Vehicle motorbike;
    Vehicle familySedan;
    Facility facility;

    @Before
    public void setup(){
        Random random = new Random(3);
        truck = VehicleGeneratorUtils.generateTruck(random);
        car = VehicleGeneratorUtils.generateSmallCar(random);
        motorbike = VehicleGeneratorUtils.generateMotorbike();
        familySedan = VehicleGeneratorUtils.generateFamilySedan(random);
        facility = new FillingStation(1);

    }

    @Test
    public void testAddVehicleToServiceMachine(){

        try {
            assertTrue(facility.addCustomerToMachine(car));
            assertEquals(1, facility.getServiceMachines()[0].getCustomersInQueue().size());

            assertTrue(facility.addCustomerToMachine(motorbike));
            assertEquals(2, facility.getServiceMachines()[0].getCustomersInQueue().size());

            assertTrue(facility.addCustomerToMachine(familySedan));
            assertEquals(3, facility.getServiceMachines()[0].getCustomersInQueue().size());

            assertFalse(facility.addCustomerToMachine(truck));
            assertEquals(3, facility.getServiceMachines()[0].getCustomersInQueue().size());

        } catch (PumpNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFindBestServiceMachine(){
        Random random = new Random();
        Vehicle truck = VehicleGeneratorUtils.generateTruck(random);
        Vehicle truck2 = VehicleGeneratorUtils.generateTruck(random);
        Vehicle truck3 = VehicleGeneratorUtils.generateTruck(random);
        Vehicle truck4 = VehicleGeneratorUtils.generateTruck(random);
        Vehicle motorbike = VehicleGeneratorUtils.generateMotorbike();
        FillingStation fillingStation = new FillingStation(3);
        try {
            assertTrue(0 == fillingStation.findBestMachine(truck));
            fillingStation.getServiceMachines()[0].addCustomer(truck);
            assertTrue(1 == fillingStation.findBestMachine(truck2));
            fillingStation.getServiceMachines()[1].addCustomer(truck2);
            assertTrue(2 == fillingStation.findBestMachine(truck3));
            fillingStation.getServiceMachines()[2].addCustomer(truck3);
            assertTrue(0 == fillingStation.findBestMachine(motorbike));
            assertTrue(-1 == fillingStation.findBestMachine(truck4));
            fillingStation.removeCustomerFromPump(0);
            assertTrue(0 == fillingStation.findBestMachine(truck));
        } catch (PumpNotFoundException e) {
            e.printStackTrace();
        }
    }
}
