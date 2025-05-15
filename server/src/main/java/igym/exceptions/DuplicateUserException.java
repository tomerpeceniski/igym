package igym.exceptions;

/**
 * Thrown when attempting to create or update a user with fields that already exist.
 */
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
