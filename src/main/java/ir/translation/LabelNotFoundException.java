package ir.translation;

/**
 * To be thrown when a given label is not present in an environment.
 */
public class LabelNotFoundException extends Exception {

    public LabelNotFoundException(String message) {
        super(message);
    }
}
