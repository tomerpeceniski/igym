package igym.exceptions;

public class GymNotFoundException extends RuntimeException {
    public GymNotFoundException(String message){
        super(message);
    }
}