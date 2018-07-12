package com.team2.petrolstation.view;

import com.team2.petrolstation.Simulator;
import com.team2.petrolstation.model.exception.InvalidInputException;
import com.team2.petrolstation.util.FileWriterUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.concurrent.TimeUnit;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.SECONDS_PER_TICK;
import static com.team2.petrolstation.util.FileWriterUtils.generateResultsFile;

/**
 * @author callummarriage
 */
public class SimulatorController {

    @FXML
    private Button run;

    @FXML
    private TextField runTime;

    @FXML
    private TextField price;

    @FXML
    private TextField numPumps;

    @FXML
    private TextField numTills;

    @FXML
    private TextField p;

    @FXML
    private TextField q;

    @FXML
    private TextField t;

    @FXML
    private TextArea activityFeed;

    @FXML
    private TextField moneyLost;

    @FXML
    private TextField moneyGained;

    @FXML
    public void submitButtonPressed() throws InvalidInputException {


        try {
            Integer numOfTurns;
            Integer duration = getIntegerValueFromField(runTime);
            Double intPrice = getDoubleValueFromField(price);
            Integer intNumPumps = 2;
            Integer intNumTills = 2;
            Double dP = getDoubleValueFromField(p);
            Double dQ = getDoubleValueFromField(q);
            Double dT = getDoubleValueFromField(t);

            if (duration == null | intPrice == null | intNumPumps == null | intNumTills == null | dP == null | dQ == null | dT == null) {
                throw new InvalidInputException("error");
            }
            Simulator simulator = new Simulator();
            numOfTurns = convertTimeIntoTicks(duration, "s");

            updateScreen("**Welcome to the simulation**\nHere are the simulation details.\nThe duration will be " + (numOfTurns * SECONDS_PER_TICK)+ " seconds or " + (numOfTurns) + " ticks.", activityFeed);
            try {
                updateScreen("Number of Pumps: " + intNumPumps + "\n" + "Number of tills: " + intNumTills + "\n", activityFeed);
                TimeUnit.MILLISECONDS.sleep(1000);
                updateScreen("The simulation will begin shortly.\n", activityFeed);
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //run the simulation using the inputed values
            simulator.setP(dP);
            simulator.setQ(dQ);
            simulator.simulate(numOfTurns, intNumPumps, intNumTills, intPrice, true, activityFeed);

            String results = simulator.getResults();

            moneyLost.setText(simulator.getMoneyLostFromShop() + "");
            moneyGained.setText(simulator.getMoneyGained() + "");
            updateScreen(results, activityFeed);
            generateResultsFile(results);
        } catch (InvalidInputException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have inputted invalid values, please ensure that you have filled in each textbox and try again.", ButtonType.YES);
            alert.showAndWait();
        }
    }

    private Integer getIntegerValueFromField(TextField object) throws InvalidInputException {
        Integer i = null;
        try {
            i = Integer.valueOf(object.getText());
        } catch (NumberFormatException ex) {
            throw new InvalidInputException("Invalid input");
        }
        return i;
    }

    private Double getDoubleValueFromField(TextField object) throws InvalidInputException {
        Double i = null;
        try {
            i = Double.valueOf(object.getText());
        } catch (NumberFormatException ex) {
            throw new InvalidInputException("Invalid input");
        }
        return i;
    }

    public void updateScreen(String results, TextArea activityFeed){
        activityFeed.setText(activityFeed.getText() + "\n" +"> " + results);
        FileWriterUtils.updateOutputFile(results + "\n");
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
    private static Integer convertTimeIntoTicks(Integer time, String identifier) throws InvalidInputException{

        Integer number;
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

            number = Integer.parseInt(Math.round(doubleNumber) + "");
        } catch (Exception e){
            throw new InvalidInputException(time + "");
        }

        return number;
    }

}
