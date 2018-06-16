package com.team2.petrolstation.model.exception;

/**
 * When a driver can not be assigned to a till
 *
 * @author callummarriage
 */
public class ServiceMachineAssigningException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceMachineAssigningException(){
        super("There was a problem handling a Driver.");
    }
}
