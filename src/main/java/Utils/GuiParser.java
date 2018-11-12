package Utils;

import Exceptions.InputFieldEmptyException;
import Exceptions.InvalidInputException;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Class providing methods to parse Textfields and Textareas
 *
 * @author Tim Leuschner
 */

public class GuiParser {

    /**
     * You shall not initialize this class
     */
    private GuiParser() {
        System.exit(1);
        //Das lassen wir uns nicht bieten!
    }

    public static String parseTextField(TextField textField, String txtFieldName, String regex) throws InputFieldEmptyException, InvalidInputException {
        String input = textField.getText().trim();

        if (input.isEmpty()) {
            throw new InputFieldEmptyException(txtFieldName);
        } else if (!input.matches(regex)) {
            throw new InvalidInputException(txtFieldName);
        }
        return input;
    }

    public static String[] parseTextArea(TextArea textArea) throws InputFieldEmptyException {
        String input = textArea.getText().trim();

        if (input.isEmpty()) {
            throw new InputFieldEmptyException("Quellen");
        }

        String[] result = input.replaceAll("\\s", "").split(",");
        return result;
    }
}
