package com.team2.petrolstation.view;

import com.team2.petrolstation.Simulator;
import com.team2.petrolstation.model.exception.InvalidInputException;
import com.team2.petrolstation.util.FileWriterUtils;
import com.team2.petrolstation.util.TimeUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
    private Spinner numPumps;

    @FXML
    private Spinner numTills;

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
    private ColorPicker colorPicker;

    @FXML
    private Pane pane;

    @FXML
    public void submitButtonPressed() throws InvalidInputException {


        try {
            activityFeed.setText("");
            moneyGained.setText("");
            moneyLost.setText("");
            Integer numOfTurns;
            Integer duration = getIntegerValueFromField(runTime);
            Double intPrice = getDoubleValueFromField(price);
            Integer intNumPumps = getIntegerValueFromSpinner(numPumps);
            Integer intNumTills = getIntegerValueFromSpinner(numTills);
            Double dP = getDoubleValueFromField(p);
            Double dQ = getDoubleValueFromField(q);
            Double dT = getDoubleValueFromField(t);

            if (duration == null | intPrice == null | intNumPumps == null | intNumTills == null | dP == null | dQ == null | dT == null) {
                throw new InvalidInputException("error");
            }
            Simulator simulator = new Simulator();
            numOfTurns = TimeUtils.convertTimeIntoTicks(duration, "s");

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
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have inputted invalid values, please ensure that you have filled in each textbox and try again.", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @FXML
    public void onColourChange(){
        pane.setBackground(new Background(new BackgroundFill(Color.web(String.valueOf(colorPicker.getValue())), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private Integer getIntegerValueFromSpinner(Spinner spinner) throws InvalidInputException {
        Integer i = null;
        try {
            i = Integer.valueOf(spinner.getValue() + "");
        } catch (NumberFormatException ex) {
            throw new InvalidInputException("Invalid input");
        }
        return i;
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
}
