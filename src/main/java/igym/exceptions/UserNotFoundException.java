package igym.exceptions;

/**
 * Thrown when a user with the specified ID does not exist or is inative.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
