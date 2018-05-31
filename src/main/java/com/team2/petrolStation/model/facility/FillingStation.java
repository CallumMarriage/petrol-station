package com.team2.petrolStation.model.facility;

import com.team2.petrolStation.model.serviceMachine.Pump;
import com.team2.petrolStation.model.serviceMachine.ServiceMachine;


/**
 * The filling station manages the refueling of all of the vehicles at the front of all of the pump queues.
 * The filling station should return a map of drivers that have finished with the pumps that their vehicles are at.
 *
 * @author callummarriage
 */
public class FillingStation extends Facility {
    public FillingStation(Integer numOfServiceMachines) {
        customerServers = new ServiceMachine[numOfServiceMachines];
        for(int i = 0; i < numOfServiceMachines; i++){
            customerServers[i] = new Pump();
        }
    }
}
