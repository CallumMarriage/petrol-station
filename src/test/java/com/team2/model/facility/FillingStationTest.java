package com.team2.model.facility;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by callummarriage on 25/05/2018.
 */
public class FillingStationTest {

    @Test
    public void testManageTransactionsForFillingStation(){

    }

    /*
    @Test
    public void testMaximumVehicles(){

        Vehicle truck = new Truck();
        Vehicle truck2 = new Truck();
        Vehicle truck3 = new Truck();

        FillingStation facility = new FillingStation(1);

        assertEquals(0, facility.findBestServiceMachine());
        facility.addCustomerToServiceMachine(0, truck);
        assertEquals(1, facility.findBestServiceMachine());
        facility.addCustomerToServiceMachine(0, truck2);
        assertEquals(-1, facility.findBestServiceMachine());
        assertFalse(facility.addCustomerToBestMachine(0, truck3));
    }
    */
}
