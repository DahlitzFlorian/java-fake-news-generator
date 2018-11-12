package Gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Utils.Popups;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        log.info(readConfig().toString());
    }

   public void openConfig() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fakenews-config.fxml"));
            Stage config = new Stage();
            config.setTitle("Konfiguration");
            config.setScene(new Scene(root, 600, 293.0));
            config.setResizable(false);
            config.show();
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
   }

   private JsonObject readConfig() {
       JsonObject configData = null;
        try {
            String text = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
            JsonReader jsonReader = Json.createReader(new StringReader(text));
            configData = jsonReader.readObject();
            jsonReader.close();
        } catch (IOException e) {
            Popups.createPopup(Alert.AlertType.ERROR, "Die Datei \"config.json\" konnte nicht geöffnet werden!", "Fehlende Konfiguration");
            log.severe(e.getMessage());
        }
        return configData;
   }

}
