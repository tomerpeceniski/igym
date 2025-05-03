package igym.exceptions;

/**
 * Custom exception to be thrown when an exercise is not found.
 */
public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
