package com.team2.petrolStation;

import javafx.application.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

/**
 * Created by ACLEAR on 04/06/2018.
 */
public class GUI extends Application{

    @Override
    public void start(Stage primaryStage) {
        try{
            Pane root = (Pane) FXMLLoader.load(
                    getClass().getResource("GUI.fxml"));
            Scene scene = new Scene(root, , 50);
            primaryStage.setTitle("Petrol Station Control Panel");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }

        /*
        Creates the grid pane that the components sit on
        final GridPane root = new GridPane();
        root.setHgap(5.0);
        root.setVgap(5.0);
        root.setPadding(new Insets(10, 10, 10, 10));

        TextField for inputting the value of p
        final TextField pValueInput = new TextField();
        pValueInput.setPromptText("Enter value");
        pValueInput.setMaxWidth(Double.MAX_VALUE);
        pValueInput.setMinimumWidth(160);
        GridPane.setHgrow(pValue, Priority.ALWAYS);
        root.add(pValue, 0, 0);

        Button to run the simulation
        final Button btnRun = new Button();
        btnRun.setMinWidth(70);
        btnRun.setText("Run");
        btnRun.setOnAction(event -> {
            Alert runAlert = new Alert(Alert.AlertType.INFORMATION);
            runAlert.setTitle("Run");
            runAlert.setContentText("The petrol station simulation is now running.");
            runAlert.showAndWait();
        });
        root.add(runBtn, 1, 0);

        Scene scene = new Scene(root, 1000, 1000);
        primaryStage.setTitle("Petrol Station Control Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
        */
    }
}
