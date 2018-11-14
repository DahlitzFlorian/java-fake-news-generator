package Gui;

import TextSynthesis.TextSynthesis;
import Utils.Popups;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Leuschner
 */

public class ControllerMainWindow {

    private static final Logger log = Logger.getLogger(ControllerMainWindow.class.getName());

    final private PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    @FXML
    public Button btnGenerateArticle;

    @FXML
    public Button btnOpenConfig;

    @FXML
    public TextField txtFieldKeywords;

    private TextSynthesis textSynthesis = new TextSynthesis();

    private GuiParser parser = new GuiParser();

    @FXML
    public void generateArticle() {
        txtFieldKeywords.pseudoClassStateChanged(errorClass, false);
        List<String> keywords = getKeywords();
        if(keywords != null) {
            textSynthesis.createArticle(keywords);
        } else {
            txtFieldKeywords.pseudoClassStateChanged(errorClass, true);
            Popups.createPopup(Alert.AlertType.ERROR, parser.getNotifications().getErrors().get(txtFieldKeywords), "Ungültige Eingabe");
            parser.getNotifications().clearErrors();
        }
    }

    @FXML
    public void openConfig() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fakenews-config.fxml"));
            Stage config = new Stage();
            config.setTitle("Konfiguration");
            Scene scene = new Scene(root, 600, 293.0);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("errors.css").toExternalForm());
            config.setScene(scene);
            config.setResizable(false);
            config.show();
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    private List<String> getKeywords() {
        List<String> result;
        String keywords = parser.parseTextField(txtFieldKeywords, "Keywords");
        if(!parser.getNotifications().hasErrors()) {
            result = Arrays.asList(keywords.split(","));
        } else {
            result = null;
        }
        return result;
    }


}
