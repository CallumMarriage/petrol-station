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
import java.util.logging.Logger;

import static com.team2.petrolstation.model.constant.PetrolStationConstants.SECONDS_PER_TICK;
import static com.team2.petrolstation.util.FileWriterUtils.generateResultsFile;

/**
 * @author callummarriage
 */
public class SimulatorController {

    private static final Logger LOGGER = Logger.getLogger(Simulator.class.getName());

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
    public TextArea activityFeed;

    @FXML
    private TextField moneyLost;

    @FXML
    private TextField moneyGained;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Pane pane;

    @FXML
    private ChoiceBox<String> identifiers;

    @FXML
    public void submitButtonPressed() throws InvalidInputException {

        try {
            activityFeed.setText("");
            moneyGained.setText("");
            moneyLost.setText("");
            Integer numOfTurns;

            Integer duration;
            Double intPrice;
            Integer intNumPumps;
            Integer intNumTills;
            Double dP;
            Double dQ;
            Double dT;
            String identifier;

            try {
                duration = Integer.valueOf(runTime.getText());
                intPrice = Double.valueOf(price.getText());
                intNumPumps = Integer.valueOf(numPumps.getValue() + "");
                intNumTills = Integer.valueOf(numTills.getValue() + "");
                dP = Double.valueOf(p.getText());
                dQ = Double.valueOf(q.getText());
                dT = Double.valueOf(t.getText());
                identifier = this.identifiers.getValue();
            }catch (NumberFormatException e){
                throw new InvalidInputException("Invalid input");
            }

            if (duration == null | intNumPumps == null | intNumTills == null) {
                throw new InvalidInputException("error");
            }

            Simulator simulator = new Simulator(intNumPumps, intNumTills, dP, dQ, dT, activityFeed);

            numOfTurns = TimeUtils.convertTimeIntoTicks(duration, identifier);

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
            simulator.simulate(numOfTurns, intPrice);
            String results = simulator.getResults();

            moneyLost.setText(simulator.getShop().getMoneyLost() + "");
            moneyGained.setText(simulator.getShop().getMoneyGained() + "");
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

    @FXML
    public void updateScreen(String results, TextArea activityFeed){
        activityFeed.setText(activityFeed.getText() + "\n" + "> " + results);
        FileWriterUtils.updateOutputFile(results + "\n");
    }
}
