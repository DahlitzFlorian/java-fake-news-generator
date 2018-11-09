package Gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    public Button btnGenerateArticle;

    @FXML
    public Button btnOpenConfig;

    @FXML
    public TextField txtFieldKeywords;

    public void generateArticle() {
        System.out.println("generatin article lol");
    }

}
