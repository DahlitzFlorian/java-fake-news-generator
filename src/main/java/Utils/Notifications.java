package Utils;


import javafx.scene.control.TextInputControl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leuschner
 */
public class Notifications {
    private Map<TextInputControl, String> errors = new HashMap<>();

    public void addError(TextInputControl t, String errorMessage) {
        errors.put(t, errorMessage);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<TextInputControl, String>  getErrors() {
        return errors;
    }

    public void clearErrors() {
        errors.clear();
    }
}
