package com.team2.petrolStation.model.exceptions;

/**
 * Created by callummarriage on 31/05/2018.
 */
public class PumpNotFoundException extends Exception {

    public PumpNotFoundException(Integer pumpNumber){
        super("Pump not found: " + pumpNumber + ".");
    }
}
