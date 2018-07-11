package com.team2.petrolstation.util;

import com.team2.petrolstation.model.view.ApplicationView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.OUTPUT_FILE;
import static com.team2.petrolstation.model.constant.PetrolStationConstants.RESULTS_DESTINATION_FILE;

/**
 * Utilities used to generate and write to files
 *
 * @author callummarriage
 */
public class FileWriterUtils {

    private static File outputFile =  new File(OUTPUT_FILE +LocalDateTime.now()+ ".txt");
    private static final Logger LOGGER = Logger.getLogger(ApplicationView.class.getName());

    private FileWriterUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Writes results to a file
     * Prints result to screen as its writing.
     * Should file generation be part of the Application or the Application View??
     *
     * @param results list of all of the results.
     */
    public static void generateResultsFile(String results){
        BufferedWriter bufferedWriter = null;
        java.io.FileWriter fileWriter = null;

        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String date= dtf.format(now);
            System.out.println(date);

            //create the file writer using the location store as a constant
            fileWriter = new java.io.FileWriter(RESULTS_DESTINATION_FILE+"-"+date+".txt");
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
    public static void updateOutputFile(String output){
        BufferedWriter bufferedWriter;
        try{

            java.io.FileWriter fw = new java.io.FileWriter(outputFile.getAbsoluteFile(), true);
            bufferedWriter= new BufferedWriter(fw);

            bufferedWriter.write(output);
            bufferedWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
