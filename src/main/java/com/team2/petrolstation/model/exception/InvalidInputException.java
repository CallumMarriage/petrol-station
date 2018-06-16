package com.team2.petrolstation.model.exception;

/**
 * Called when a invalid from the gui has been detected
 *
 * @author callummarriage
 */
public class InvalidInputException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(String time){
        super(time + " is not a valid input for the time field.");
    }
}
