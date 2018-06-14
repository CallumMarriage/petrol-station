package com.team2.petrolStation.model.views;

import com.team2.petrolStation.model.facility.FillingStation;
import com.team2.petrolStation.model.facility.Shop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.team2.petrolStation.model.constants.PetrolStationConstants.RESULTS_DESTINATION_FILE;

/**
 * Created by callummarriage on 13/06/2018.
 */
public class TextView implements SimulatorView {

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
