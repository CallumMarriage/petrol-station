package com.team2.petrolStation.model.exceptions;

/**
 * Called when a invalid from the gui has been detected
 *
 * @author callummarriage
 */
public class InvalidInputException extends Exception{

    public InvalidInputException(String time){
        super(time + " is not a valid input for the time field.");
    }
}
