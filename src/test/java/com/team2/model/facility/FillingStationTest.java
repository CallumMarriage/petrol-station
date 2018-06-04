package com.team2.model.facility;

import com.team2.petrolStation.model.customer.vehicle.Truck;
import com.team2.petrolStation.model.customer.vehicle.Vehicle;
import com.team2.petrolStation.model.facility.FillingStation;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;


/**
 * Created by callummarriage on 25/05/2018.
 */
public class FillingStationTest {

    @Test
    public void testManageTransactionsForFillingStation(){

    }

    @Test
    public void testMaximumVehicles(){

        Random random = new Random(2);
        Vehicle truck = new Truck(random);
        Vehicle truck2 = new Truck(random);
        Vehicle truck3 = new Truck(random);

        FillingStation facility = new FillingStation(2);

        try {
            assertEquals(0, facility.findBestMachine(truck));
            facility.addCustomerToBestMachine(0, truck);
            assertEquals(1, facility.findBestMachine(truck2));
            facility.addCustomerToBestMachine(1, truck2);
            assertEquals(-1, facility.findBestMachine(truck3));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
