package Gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class ControllerMainWindow {

    private static final Logger log = Logger.getLogger(ControllerMainWindow.class.getName());

    @FXML
    public Button btnGenerateArticle;

    @FXML
    public Button btnOpenConfig;

    @FXML
    public TextField txtFieldKeywords;

    public void generateArticle() {
        log.info("Generating Article");
    }

   public void openConfig() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("path/to/other/view.fxml"));
            Stage config = new Stage();
            config.setTitle("Konfiguration");
            config.setScene(new Scene(root, 450, 450));
            config.show();
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
   }

}
