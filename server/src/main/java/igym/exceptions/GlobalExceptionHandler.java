package igym.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 *
 * <p>
 * This class uses Spring's {@link ControllerAdvice} to catch and process
 * exceptions thrown across all controllers,
 * returning standardized JSON error responses for both validation errors and
 * custom exceptions.
 * </p>
 *
 * <p>
 * Each exception handler method builds a consistent response structure that
 * includes:
 * <ul>
 * <li>timestamp – the time the error occurred</li>
 * <li>status – the HTTP status code</li>
 * <li>error – the standard reason phrase</li>
 * <li>message – a descriptive error message</li>
 * </ul>
 * </p>
 *
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Map<String, Object> buildResponseBody(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }

    /**
     * Handles validation errors triggered by bean validation (@Valid).
     *
     * @param ex the exception containing validation errors
     * @return a {@link ResponseEntity} with status 422 Unprocessable Entity,
     *         containing details about the validation failures
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = buildResponseBody(HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getDefaultMessage()).toList();

        body.put("message", "Validation error");
        body.put("errors", errors);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /**
     * Handles cases where a gym with the same validated fields already exists.
     *
     * @param ex the thrown {@link DuplicateGymException}
     * @return a {@link ResponseEntity} with status 409 Conflict and error details
     */
    @ExceptionHandler(DuplicateGymException.class)
    public ResponseEntity<Object> handleDuplicateGymException(DuplicateGymException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Handles cases where a user with the same validated fields already exists.
     *
     * @param ex the thrown {@link DuplicateUserException}
     * @return a {@link ResponseEntity} with status 409 Conflict and error details
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Object> handleDuplicateUserException(DuplicateUserException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Handles requests for gyms that do not exist in the system or are inactive.
     *
     * @param ex the thrown {@link GymNotFoundException}
     * @return a {@link ResponseEntity} with status 404 Not Found and error details
     */
    @ExceptionHandler(GymNotFoundException.class)
    public ResponseEntity<Object> handleGymNotFoundException(GymNotFoundException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles requests for users that do not exist in the system or are inative.
     *
     * @param ex the thrown {@link UserNotFoundException}
     * @return a {@link ResponseEntity} with status 404 Not Found and error details
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles all uncaught exceptions not covered by more specific handlers.
     *
     * @param ex the thrown {@link Exception}
     * @return a {@link ResponseEntity} with status 500 Internal Server Error and
     *         error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        Map<String, Object> body = buildResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Handles requests for workouts that do not exist in the system or are
     * inactive.
     *
     * @param ex the thrown {@link WorkoutNotFoundException}
     * @return a {@link ResponseEntity} with status 404 Not Found and error details
     */
    @ExceptionHandler(WorkoutNotFoundException.class)
    public ResponseEntity<Object> handleWorkoutNotFoundException(WorkoutNotFoundException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles requests for exercises that do not exist in the system or are
     * inactive.
     *
     * @param ex the thrown {@link ExerciseNotFoundException}
     * @return a {@link ResponseEntity} with status 404 Not Found and error details
     */
    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<Object> handleExerciseNotFoundException(ExerciseNotFoundException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialException(InvalidCredentialsException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<Object> handleInvalidNameException(InvalidNameException ex) {
        logger.error(ex.getMessage());
        Map<String, Object> body = buildResponseBody(HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        List<String> errors = List.of(ex.getMessage().split(", "));

        body.put("message", "Validation error");
        body.put("errors", errors);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }
}