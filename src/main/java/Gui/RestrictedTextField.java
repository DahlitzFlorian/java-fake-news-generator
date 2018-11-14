package Gui;

import javafx.scene.control.TextField;

/**
 * @author Leuschner
 */

public class RestrictedTextField extends TextField {

    private int limitCharacters;

    public RestrictedTextField() {
        this.limitCharacters = 3;
    }

    public void setLimit(int limit) {
        this.limitCharacters = limit;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, text);
        verify();
    }

    @Override
    public void replaceSelection(String text) {
        super.replaceSelection(text);
        verify();
    }

    private void verify() {
        if(!getText().matches("\\d*")) {
            setText(getText().replaceAll("\\D*", ""));
            positionCaret(getText().length());
        }
        if (getText().length() > limitCharacters) {
            setText(getText().substring(0, limitCharacters));
            positionCaret(getText().length());
        }
    }
}
