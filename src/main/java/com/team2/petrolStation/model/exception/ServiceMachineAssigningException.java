package com.team2.petrolStation.model.exception;

/**
 * When a driver can not be assigned to a till
 *
 * @author callummarriage
 */
public class ServiceMachineAssigningException extends Exception {

    public ServiceMachineAssigningException(){
        super("There was a problem handling a Driver.");
    }
}
