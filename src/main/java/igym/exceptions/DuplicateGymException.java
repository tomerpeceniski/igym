package igym.exceptions;

public class DuplicateGymException extends RuntimeException {
    public DuplicateGymException(String message) {
        super(message);
    }
}