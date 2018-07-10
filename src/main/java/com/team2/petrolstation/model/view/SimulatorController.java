package com.team2.petrolstation.model.view;

import com.team2.petrolstation.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by callummarriage on 10/07/2018.
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
    public void submitButtonPressed(){

        Integer intDuration = getIntegerValueFromField(runTime);
        Double intPrice = getDoubleValueFromField(price);
        Integer intNumPumps = getIntegerValueFromField(numPumps);
        Integer intNumTills = getIntegerValueFromField(numTills);
        Double dP = getDoubleValueFromField(p);
        Double dQ = getDoubleValueFromField(q);
        Double dT = getDoubleValueFromField(t);

        Application application = new Application();
        application.simulate(intDuration, intNumPumps, intNumTills, intPrice, true);
        System.out.println(intDuration + intPrice);
    }

    private Integer getIntegerValueFromField(TextField object) {
        Integer i = null;
        try {
            i = Integer.valueOf(object.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return i;
    }

    private Double getDoubleValueFromField(TextField object) {
        Double i = null;
        try {
            i = Double.valueOf(object.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return i;
    }

}
