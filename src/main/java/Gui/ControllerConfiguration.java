package Gui;

import Configuration.Configuration;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Utils.Popups;
import javafx.stage.Stage;


import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author Leuschner
 */

public class ControllerConfiguration implements Initializable {

    private static final Logger log = Logger.getLogger(ControllerMainWindow.class.getName());

    final private PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    @FXML
    public Button btnSaveConfig;

    @FXML
    public TextArea txtAreaNewsSources;

    @FXML
    public RestrictedTextField txtFieldMaxWordCount;

    @FXML
    public RestrictedTextField txtFieldMinWordCount;

    private GuiParser parser = new GuiParser();

    private JsonObject configJson = null;

    private Configuration configuration = new Configuration();

    private int distance;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            configJson = configuration.getTextConfigurations();
            txtAreaNewsSources.setText(String.join(",\n", configuration.getSources(configJson))
                    .replaceAll("\"", ""));
            txtFieldMinWordCount.setText(configJson.getInt("min") + "");
            txtFieldMaxWordCount.setText(configJson.getInt("max") + "");
            distance = configJson == null ? 50 : configJson.getInt("min_distance");
        } catch (IOException e) {
            Popups.createPopup(Alert.AlertType.INFORMATION,
                    "Konfiguration konnte nicht gefunden werden!\n Neue Konfiguration wird erstellt!",
                    "Keine Konfiguration vorhanden");
            log.info(e.getMessage());
        }

    }

    /**
     * Saves the parameters provided by the user into JSON File
     */
    @FXML
    public void saveConfig() {
        configJson = generateJson();
        if (configJson != null) {
            try {
                configuration.saveConfiguration(configJson);
                Stage stage = (Stage) btnSaveConfig.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                Popups.createPopup(Alert.AlertType.ERROR, e.getMessage(), "Konnte nicht auf Datei zugreifen");
            }
        } else {
            log.info("Eingaben fehlerhaft -> konnte nicht gespeichert werden");
        }

    }

    private JsonObject generateJson() {
        resetStyles();

        //parse text fields
        List<String> sources = parser.parseTextArea(txtAreaNewsSources);
        String maxWordsString = parser.parseTextField(txtFieldMaxWordCount, "Max Wörter");
        String minWordsString = parser.parseTextField(txtFieldMinWordCount, "Min. Wörter");

        //check for errors
        if (!validate(maxWordsString, minWordsString)) return null;

        JsonArrayBuilder sourcesArray = Json.createArrayBuilder();
        for (String source : sources) {
            sourcesArray.add(source);
        }

        //if everything's good -> generate JSON
        JsonObject result = Json.createObjectBuilder()
                .add("min", Integer.parseInt(minWordsString))
                .add("max", Integer.parseInt(maxWordsString))
                .add("min_distance", 50)
                .add("sources", sourcesArray.build())
                .build();
        log.info(result.toString());
        return result;

    }

    private void resetStyles() {
        txtAreaNewsSources.pseudoClassStateChanged(errorClass, false);
        txtFieldMaxWordCount.pseudoClassStateChanged(errorClass, false);
        txtFieldMinWordCount.pseudoClassStateChanged(errorClass, false);
    }

    /**
     * Checks whether the user's input is valid data or not
     *
     * @return true if no errors are found, false if errors are present
     */
    private boolean validate(String maxWordsString, String minWordsString) {
        boolean result = true;
        StringBuilder errorMessage = new StringBuilder();

        //If there is valid input in the textfields check for min/max requirements
        if (!parser.getNotifications().hasErrors()) {
            int minWords = Integer.parseInt(minWordsString);
            int maxWords = Integer.parseInt(maxWordsString);
            if (minWords < 50 || minWords > 750 - distance)
                parser.getNotifications().addError(txtFieldMinWordCount, "Min. Wörter muss zwischen 50 und " + (750 - distance) + " sein!\n");
            if (maxWords > 750 || maxWords < 50 + distance)
                parser.getNotifications().addError(txtFieldMaxWordCount, "Max. Wörter muss zwischen " + (50 + distance) + " und 750 sein!\n");
            if (minWords + distance >= maxWords) {
                //adding new textfield to not overwrite previous errors, not elegant but works
                parser.getNotifications().addError(new TextField(), "Max. Wörter muss mindestens " + distance + " Wörter länger sein als Min. Wörter!\n");
                txtFieldMaxWordCount.pseudoClassStateChanged(errorClass, true);
                txtFieldMinWordCount.pseudoClassStateChanged(errorClass, true);
            }

        }

        //if errors are present after checking max/min count
        if (parser.getNotifications().hasErrors()) {
            //Mark invalid inputs red
            for (TextInputControl t : parser.getNotifications().getErrors().keySet()) {
                t.pseudoClassStateChanged(errorClass, true);
            }

            //Build error messages and display them
            for (String s : parser.getNotifications().getErrors().values()) errorMessage.append(s);
            Popups.createPopup(Alert.AlertType.ERROR, errorMessage.toString(), "Fehlerhafte Eingaben");

            //Cleanup
            parser.getNotifications().clearErrors();
            result = false;
        }

        return result;
    }

    /*
    Palms are sweaty
    knees weak arms are heavy
    bugs littering his code already
    code's spaghetti 🍝
     */
}
