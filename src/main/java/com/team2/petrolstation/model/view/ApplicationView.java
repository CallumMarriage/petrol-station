package com.team2.petrolstation.model.view;

import com.team2.petrolstation.model.exception.InvalidInputException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.*;

/**
 * @author callummarriage
 */
public class ApplicationView {

    private static final Logger LOGGER = Logger.getLogger(ApplicationView.class.getName());
    private Simulator simulator;
    private static File outputFile;

    public ApplicationView(Simulator simulator){
        this.simulator = simulator;
        try {
            outputFile = new File(OUTPUT_FILE +LocalDateTime.now()+ ".txt");

            PrintWriter writer = new PrintWriter(outputFile);
            writer.print("");
            writer.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Start method intakes the values and kicks of the simulation using the values
     *
     */
    public void start(){
        //replace this with gui values
        Integer numOfTurns;
        Integer numPumps = 1;
        Integer numTills = 1;
        Double priceOfFuel = 1.2;
        Double p = 0.01;
        Double q = 0.02;
        Boolean truckIsActive = true;
        String time = "1";
        String identifiers = "h";

        try {
            numOfTurns = convertTimeIntoTicks(time, identifiers);

        } catch(InvalidInputException e){
            e.printStackTrace();
            return;
        }
        updateScreen("**Welcome to the simulation**\nHere are the simulation details.\nThe duration will be " + (numOfTurns * SECONDS_PER_TICK)+ " seconds or " + (numOfTurns) + " ticks.");
        try {
            updateScreen("Number of Pumps: " + numPumps + "\n" + "Number of tills: " + numTills + "\n");
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("The simulation will begin shortly.\n");
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //run the simulation using the inputed values
        this.simulator.simulate(numOfTurns, numPumps, numTills, priceOfFuel, p, q, truckIsActive);

        String results = simulator.getResults();
        updateScreen(results);
        generateResultsFile(results);
    }

    public void updateScreen(String results){
        System.out.println("> " + results);

        updateOutputFile(results + "\n");
        //LOGGER.log(Level.INFO, results);
    }

    /**
     * Converts the time into seconds based on its identifier and returns the new value.
     * I chose this design because it is clear what is calculating what, and is easy to add new time formats
     *
     * Should this be in application?
     *
     * @param time the amount of time the simulation will execute for.
     * @return time in seconds
     * @throws InvalidInputException time could not be converted.
     */
    private static Integer convertTimeIntoTicks(String time, String identifier) throws InvalidInputException{

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
                    default: return null;
                }

                doubleNumber /= SECONDS_PER_TICK;
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
     * Should file generation be part of the Application or the Application View??
     *
     * @param results list of all of the results.
     */
    private void generateResultsFile(String results){
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try{
            LocalDateTime currentDate = LocalDateTime.now();
            //create the file writer using the location store as a constant
            fileWriter = new FileWriter(RESULTS_DESTINATION_FILE+"-"+currentDate+".txt");
            //create a buffered write with the file writer as an argument
            bufferedWriter = new BufferedWriter(fileWriter);
            //loop through the results list
            //add the line to the file
            bufferedWriter.write(results);

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
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    /**
     * Adds to the output to the output file
     *
     * @param output the content to be added to file.
     */
    private void updateOutputFile(String output){
        BufferedWriter bufferedWriter;
        try{

            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile(), true);
            bufferedWriter= new BufferedWriter(fw);

            bufferedWriter.write(output);
            bufferedWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
