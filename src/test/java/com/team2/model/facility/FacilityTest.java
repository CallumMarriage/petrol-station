package com.team2.model.facility;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.vehicle.*;
import com.team2.petrolStation.model.exceptions.ServiceMachineAssigningException;
import com.team2.petrolStation.model.facility.FillingStation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;


/**
 * Tests for Facility class
 *
 * @author callummarriage
 */
public class FacilityTest {


    @Test
    public void testFindBestAvailableServiceMachine(){
        Double priceOfFuel = 1.2;
        Random random = new Random(3);
        Vehicle truck = new Truck(random);
        Vehicle car = new SmallCar(random);
        Vehicle motorbike = new Motorbike();
        Vehicle familySedan = new FamilySedan(random);

        FillingStation facility = new FillingStation(2);

        Collection<Customer> vehicles = new ArrayList<Customer>();
        vehicles.add(truck);
        vehicles.add(car);

        assertEquals(0, facility.getServiceMachines()[0].getCustomersInQueue().size());
        assertEquals(0, facility.getServiceMachines()[1].getCustomersInQueue().size());

        try {
            facility.addCustomerToMachine(vehicles, priceOfFuel);
        } catch (ServiceMachineAssigningException e) {
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
            facility.addCustomerToMachine(vehicles, priceOfFuel);
        } catch (ServiceMachineAssigningException e){
            e.printStackTrace();
        }

        assertEquals(1, facility.getServiceMachines()[0].getCustomersInQueue().size());
        assertEquals(3, facility.getServiceMachines()[1].getCustomersInQueue().size());

        facility.printLeftOverCustomers();
    }
}
