package com.team2.petrolStation.model.exception;

/**
 * When a pump associated with a driver can not be found
 *
 * @author callummarriage
 */
public class PumpNotFoundException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PumpNotFoundException(Integer pumpNumber){
        super("Pump not found: " + pumpNumber + ".");
    }
}
