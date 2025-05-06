package igym.exceptions;

/**
 * Thrown when a workout with the specified ID does not exist or is inactive.
 */
public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}
