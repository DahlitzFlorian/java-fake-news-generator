package Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class LaunchGui extends Application {

    private static final Logger log = Logger.getLogger(LaunchGui.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fakenews.fxml"));
            primaryStage.setTitle("Fake News Generator 2000 und 1");
            primaryStage.setScene(new Scene(root, 605, 151));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            log.severe(e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
