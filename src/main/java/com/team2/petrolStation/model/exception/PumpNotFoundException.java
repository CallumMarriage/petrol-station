package com.team2.petrolStation.model.exception;

/**
 * When a pump associated with a driver can not be found
 *
 * @author callummarriage
 */
public class PumpNotFoundException extends Exception {

    public PumpNotFoundException(Integer pumpNumber){
        super("Pump not found: " + pumpNumber + ".");
    }
}
