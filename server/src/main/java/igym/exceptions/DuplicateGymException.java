package igym.exceptions;

/**
 * Thrown when attempting to create or update a gym with fields that already exist for the current user.
 */
public class DuplicateGymException extends RuntimeException {
    public DuplicateGymException(String message) {
        super(message);
    }
}