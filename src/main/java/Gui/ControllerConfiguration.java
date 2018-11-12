package Gui;

import Exceptions.InputFieldEmptyException;
import Exceptions.InvalidInputException;
import Utils.GuiParser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Utils.Popups;
import javafx.stage.Stage;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Tim Leuschner
 */

public class ControllerConfiguration {

    private static final Logger log = Logger.getLogger(ControllerMainWindow.class.getName());

    @FXML
    public Button btnSaveConfig;

    @FXML
    public TextArea txtAreaNewsSources;

    @FXML
    public TextField txtFieldMaxWordCount;

    @FXML
    public TextField txtFieldMinWordCount;


    /**
     * Saves the parameters provided by the user into
     */
    public void saveConfig() {
        JsonObject configJson = generateJson();
        if (configJson != null) {
            try (FileWriter file = new FileWriter("config.json")) {
                file.write(configJson.toString());
                log.info("Konfiguration erfolgreich gespeichert!");
                Stage config = (Stage) btnSaveConfig.getScene().getWindow();
                config.close();
            } catch (IOException e) {
                Popups.createPopup(Alert.AlertType.ERROR, e.getMessage(), "Konnte nicht auf Datei zugreifen");
            }
        } else {
            log.info("Eingaben fehlerhaft -> konnte nicht gespeichert werden");
        }
    }

    private JsonObject generateJson() {
        String maxWordsString = null;
        String minWordsString = null;
        String[] sources = null;
        boolean validData = true;
        JsonObject result;

        try {
            sources = GuiParser.parseTextArea(txtAreaNewsSources);
            maxWordsString = GuiParser.parseTextField(txtFieldMaxWordCount, "Max Wörter", "\\d*");
            minWordsString = GuiParser.parseTextField(txtFieldMinWordCount, "Min. Wörter", "\\d*");
        } catch (InputFieldEmptyException | InvalidInputException e) {
            Popups.createPopup(Alert.AlertType.ERROR, e.getMessage(), "Eingabe ungültig!");
            log.severe(e.getMessage());
            validData = false;
        }

        if (validData) {
            int minWords = minWordsString.length() > 3 ? Integer.parseInt(minWordsString.substring(0, 3)) : Integer.parseInt(minWordsString);
            int maxWords = maxWordsString.length() > 3 ? Integer.parseInt(maxWordsString.substring(0, 3)) : Integer.parseInt(maxWordsString);
            if (maxWords - minWords <= 0) {
                Popups.createPopup(Alert.AlertType.ERROR, "Max. Wörter muss größer sein als Min. Wörter!", "Ungültige Eingabe");
                log.severe("Max. Wörter war kleiner Min. Wörter");
                return null;
            }

            result = Json.createObjectBuilder()
                    .add("min", minWords)
                    .add("max", maxWords)
                    .add("min_distance", 50)
                    .add("sources" ,Json.createArrayBuilder()
                            .add(String.join("\",\"", sources)))
                    .build();
            log.info(result.toString());
            return result;
        } else {
            return null;
        }
    }
}
