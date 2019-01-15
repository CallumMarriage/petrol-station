package com.team2.petrolstation.model.facility;

import com.team2.petrolstation.model.customer.Vehicle;
import com.team2.petrolstation.util.VehicleGeneratorUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.team2.petrolstation.model.constant.VehicleConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author callummarriage
 */
public class FillingStationTest {

    private FillingStation fillingStation;

    @Before
    public void setup(){
        fillingStation = new FillingStation(2);

    }
    @Test
    public void testManageTransactionsForFillingStation(){

    }

    @Test
    public void testMaximumVehicles(){

        Random random = new Random(2);
        Vehicle truck = VehicleGeneratorUtils.generateTruck(random);
        truck.setIsTruck();
        Vehicle truck2 = VehicleGeneratorUtils.generateTruck(random);
        truck2.setIsTruck();
        Vehicle truck3 = VehicleGeneratorUtils.generateTruck(random);
        truck3.setIsTruck();

        FillingStation facility = new FillingStation(2);

        try {
            assertEquals(0, facility.findBestMachine(truck));
            facility.getServiceMachines()[0].addCustomer(truck);
            assertEquals(1, facility.findBestMachine(truck2));
            facility.getServiceMachines()[1].addCustomer(truck2);
            assertEquals(-1, facility.findBestMachine(truck3));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAddVehicleToPetrolStation(){
        fillingStation = new FillingStation(1);

        Random random = new Random(2);

        Vehicle truck = VehicleGeneratorUtils.generateTruck(random);
        Vehicle truck2 = VehicleGeneratorUtils.generateTruck(random);
        Vehicle truck3 = VehicleGeneratorUtils.generateTruck(random);

        fillingStation.addVehicleToPetrolStation(truck, 1.0);
        assertTrue(fillingStation.getMoneyLost() == 0.0);
        fillingStation.addVehicleToPetrolStation(truck2, 1.0);
        assertTrue(fillingStation.getMoneyLost() == 31.0);
        fillingStation.addVehicleToPetrolStation(truck3, 62.0);


    }
}
