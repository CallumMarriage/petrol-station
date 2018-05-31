package com.team2.petrolStation.model.exceptions;

/**
 * Created by callummarriage on 31/05/2018.
 */
public class ServiceMachineAssigningException extends Exception {

    public ServiceMachineAssigningException(){
        super("There was a problem handling a Driver.");
    }
}
