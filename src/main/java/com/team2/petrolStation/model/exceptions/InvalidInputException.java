package com.team2.petrolStation.model.exceptions;

/**
 * Created by callummarriage on 31/05/2018.
 */
public class InvalidInputException extends Exception{

    public InvalidInputException(String time){
        super(time + " is not a valid input for the time field.");
    }
}
