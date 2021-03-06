package seedu.address.model.healthworker.exceptions;

/**
 * Indicated that the operation exceeds the request limit in healthworker.
 */
public class RequestLimitExceededException extends Exception {
    public RequestLimitExceededException() {
        super("Operations would result in more orders than limit.");
    }
}
