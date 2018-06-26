package com.team2.petrolstation.model.servicemachine;

import com.team2.petrolstation.model.customer.Vehicle;
import org.junit.Test;

import static com.team2.petrolstation.model.constant.VehicleConstants.SIZE_OF_MOTORBIKE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by canershefik on 07/06/2018.
 */
public class PumpTest {


    @Test
    public void testAddCustomer(){
        Pump pump = new Pump();

        assertEquals(0, pump.getCustomersInQueue().size());


        for(int i = 0; i < 2; i++){
            pump.addCustomer(new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0));
        }

        assertEquals(2, pump.getCustomersInQueue().size());

        pump.addCustomer(new Vehicle(0, 0, 5, 0.0, SIZE_OF_MOTORBIKE, 0));

        assertEquals(3, pump.getCustomersInQueue().size());

    }
}
