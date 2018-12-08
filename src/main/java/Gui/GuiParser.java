package Gui;

import Utils.Notifications;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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
        String input = textArea.getText();

        if (input.isEmpty()) {
            notifications.addError(textArea, "Das Eingabefeld Quellen muss ausgefüllt werden!\n");
        }

        List<String> sources = new ArrayList<>();

        Arrays.stream(input.split(",")).forEach(source -> {
            sources.addAll(Arrays.asList(source.trim().split("\\n")));
        });

        return sources;
    }

    Notifications getNotifications() {
        return notifications;
    }
}
