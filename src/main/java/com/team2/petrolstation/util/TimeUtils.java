package com.team2.petrolstation.util;

import com.team2.petrolstation.model.exception.InvalidInputException;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.SECONDS_PER_TICK;

/**
 * @author callummarriage
 */
public class TimeUtils {
    /**
     * Converts the time into seconds based on its identifier and returns the new value.
     * I chose this design because it is clear what is calculating what, and is easy to add new time formats
     *
     * Should this be in application?
     *
     * @param time the amount of time the simulation will execute for.
     * @param identifier identifier
     * @return time in seconds
     * @throws InvalidInputException time could not be converted.
     */
    public static Integer convertTimeIntoTicks(Integer time, String identifier) throws InvalidInputException{
        try {
            Double doubleNumber = Double.parseDouble(time + "");
            //Do a check on the identifier each time, if its a day you will need to do all of the calculations,
            //This is clearer as you can see what each calculation is doing to the number even though the added if statements add additional run time.
            if(identifier.equals("t")){
                doubleNumber *= 10;
            } else {
                switch (identifier){
                    //year
                    case ("y"): doubleNumber *= 31536000;
                        break;
                    //week
                    case ("w"): doubleNumber *= 604800;
                        break;
                    //day
                    case ("d"): doubleNumber *= 86400;
                        break;
                    //hour
                    case ("h"): doubleNumber *= 3600;
                        break;
                    //minute
                    case ("m"): doubleNumber *= 60;
                        break;
                }

                doubleNumber /= SECONDS_PER_TICK;
            }

            return Integer.parseInt(Math.round(doubleNumber) + "");
        } catch (Exception e){
            throw new InvalidInputException(time + "");
        }
    }
}
