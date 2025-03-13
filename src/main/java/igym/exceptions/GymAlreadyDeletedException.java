package igym.exceptions;

public class GymAlreadyDeletedException extends RuntimeException {
    public GymAlreadyDeletedException(String message) {
        super(message);
    }
}
