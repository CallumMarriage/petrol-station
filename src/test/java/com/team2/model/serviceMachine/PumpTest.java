package com.team2.model.serviceMachine;

import com.team2.petrolStation.model.customer.Customer;
import com.team2.petrolStation.model.customer.Driver;
import com.team2.petrolStation.model.serviceMachine.ServiceMachine;
import com.team2.petrolStation.model.serviceMachine.Pump;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by canershefik on 07/06/2018.
 */
public class PumpTest {


    @Ignore
    @Test
    public void testAddCustomer(){
        Pump pump = new Pump();

        assertEquals(0, pump.getCustomersInQueue().size());


        for(int i = 0; i < 2; i++){
            //pump.addCustomer();
        }

        assertEquals(5, pump.getCustomersInQueue().size());


    }
}
