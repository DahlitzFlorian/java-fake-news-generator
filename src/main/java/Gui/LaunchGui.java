package Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaunchGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fakenews.fxml"));
        primaryStage.setTitle("Fake News Generator 2000 und 1");
        primaryStage.setScene(new Scene(root, 605, 151));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
