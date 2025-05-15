package igym.exceptions;

/**
 * Thrown when a gym with the specified ID does not exist or is inactive.
 */
public class GymNotFoundException extends RuntimeException {
    public GymNotFoundException(String message){
        super(message);
    }
}