package com.team2.petrolStation.model.views;

import com.team2.petrolStation.model.exceptions.InvalidInputException;
import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.RESULTS_DESTINATION_FILE;
import static com.team2.petrolStation.model.constants.PetrolStationConstants.SECONDS_PER_TICK;

/**
 * @author callummarriage
 */
public class Text {

    private Simulator simulator;
    public Text(Simulator simulator){
        this.simulator = simulator;
    }


    public void start(){
        //replace this with gui values
        Integer numOfTurns;
        Integer numPumps = 1;
        Integer numTills = 1;
        Double priceOfFuel = 1.2;
        Double p = 0.01;
        Double q = 0.02;

        String time = "1";
        String identifiers = "d";

        try {
            numOfTurns = convertTimeIntoSeconds(time, identifiers);

        } catch(InvalidInputException e){
            e.printStackTrace();
            return;
        }

        System.out.println("Welcome to the simulation\nThe duration will be " + numOfTurns + " seconds or " + (numOfTurns / SECONDS_PER_TICK) + " ticks \n");

        //run the simulation using the inputed values
        simulator.simulate(numOfTurns, numPumps, numTills, priceOfFuel, p, q);
    }


    public void printFinalResults(Shop shop, FillingStation fillingStation, Double moneyLost, Double moneyGained){
        List<String> results = getResults(shop, fillingStation, moneyLost, moneyGained);
        printResultsToScreen(results);
        generateFile(results);
    }
    /**
     * Get Results based on performance
     *
     * @param shop shop
     * @param fillingStation filling station
     * @return return all of the contents
     */
    private List<String> getResults(Shop shop, FillingStation fillingStation, Double moneyLost, Double moneyGained){
        List<String> results = new ArrayList<>();
        //just to make the results look cleaner make sure the right ending word is used
        String vehicle = checkIfPlural(fillingStation.getLeftOverCustomers(), "vehicle");
        String driver = checkIfPlural(shop.getShopFloor().size(), "driver");
        String tillDrivers = checkIfPlural(shop.getLeftOverCustomers(), "driver");

        //finally print out the money lost and gained.
        results.add("Results\n");
        results.add("Money lost: " + moneyLost + "\n");
        results.add("Money gained: " + moneyGained + "\n");
        results.add("Filling Station - Pumps: " + fillingStation.getLeftOverCustomers() + " " + vehicle + "\n");
        results.add("Shop - Tills: " + shop.getLeftOverCustomers() + " " + driver +"\n");
        results.add("Shop - floor: " + shop.getShopFloor().size() + " " + tillDrivers);

        return results;
    }

    private void printResultsToScreen(List<String> results){
        for(String line : results){
            System.out.println(line);
        }
    }

    private String checkIfPlural(Integer num, String word){
        if(num > 1 || num == 0 ){
            word += "s";
        }

        return word;
    }

    /**
     * Converts the time into seconds based on its identifier and returns the new value.
     * I chose this design because it is clear what is calculating what, and is easy to add new time formats
     *
     * @param time the amount of time the simulation will execute for.
     * @return time in seconds
     * @throws InvalidInputException time could not be converted.
     */
    private static Integer convertTimeIntoSeconds(String time, String identifier) throws InvalidInputException{

        Integer number;
        try {
            Double doubleNumber = Double.parseDouble(time);
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
                    //tick
                    case ("t"): doubleNumber *=10;
                    default: break;
                }
            }

            number = Integer.parseInt(Math.round(doubleNumber) + "");
        } catch (Exception e){
            throw new InvalidInputException(time);
        }

        return number;
    }

    /**
     * Writes results to a file
     * Prints result to screen as its writing.
     *
     * @param results list of all of the results.
     */
    private void generateFile(List<String> results){
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try{
            //create the file writer using the location store as a constant
            fileWriter = new FileWriter(RESULTS_DESTINATION_FILE);
            //create a buffered write with the file writer as an argument
            bufferedWriter = new BufferedWriter(fileWriter);
            //loop through the results list
            for(String message : results){
                //add the line to the file
                bufferedWriter.write(message);
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                //close the writers
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
                if(fileWriter != null){
                    fileWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
