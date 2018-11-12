package Exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String inputField) {
        super("Fehlerhafte Eingabe in " + inputField);
    }
}
