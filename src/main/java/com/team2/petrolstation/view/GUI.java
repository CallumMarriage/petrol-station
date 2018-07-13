package com.team2.petrolstation.view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author callummarriage
 */
public class GUI extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/GUI.fxml"));
        fxmlLoader.setController(new SimulatorController());
        Parent root = fxmlLoader.load();

        final Scene scene = new Scene(root, 800, 747);

        primaryStage.setTitle("The Petrol Station Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
