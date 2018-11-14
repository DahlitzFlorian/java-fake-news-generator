package Gui;

import Utils.Notifications;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class providing methods to parse Textfields and Textareas
 *
 * @author Leuschner
 */

class GuiParser {

    private Notifications notifications = new Notifications();

    String parseTextField(TextField textField, String txtFieldName) {
        String input = textField.getText();

        if (input.isEmpty()) {
            notifications.addError(textField, "Das Eingabefeld " + txtFieldName + " muss ausgefüllt werden!\n");
        }
        return input;
    }

    List<String> parseTextArea(TextArea textArea) {
        String input = textArea.getText().replaceAll("\\s", "");

        if (input.isEmpty()) {
            notifications.addError(textArea, "Das Eingabefeld Quellen muss ausgefüllt werden!\n");
        }

        return new ArrayList<>(Arrays.asList(input.split(",")));
    }

    Notifications getNotifications() {
        return notifications;
    }
}
