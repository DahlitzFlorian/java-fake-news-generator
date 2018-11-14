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
 * @author Tim Leuschner
 */

 class GuiParser {

    private Notifications notifications = new Notifications();

     GuiParser() {

    }

     String parseTextField(TextField textField, String txtFieldName, String regex) {
        String input = textField.getText().trim();

        if (input.isEmpty()) {
            notifications.addError(textField, "Das Eingabefeld " + txtFieldName + " muss ausgefüllt werden!");
        } else if (!input.matches(regex)) {
            notifications.addError(textField, "Fehlerhafte Eingabe in " + txtFieldName + "!");
        }
        return input;
    }

     List<String> parseTextArea(TextArea textArea) {
        String input = textArea.getText().replaceAll("\\s", "");

        if (input.isEmpty()) {
            notifications.addError(textArea, "Das Eingabefeld Quellen muss ausgefüllt werden!");
        }

        return new ArrayList<>(Arrays.asList(input.split(",")));
    }

     Notifications getNotifications() {
        return notifications;
    }
}
