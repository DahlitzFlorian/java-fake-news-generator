package Exceptions;

public class InputFieldEmptyException extends Exception {
    public InputFieldEmptyException(String fieldName) {
        super("Das Eingabefeld " + fieldName + " muss ausgefüllt werden!");
    }
}
