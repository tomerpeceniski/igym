package igym.controllers;

import igym.entities.Workout;
import igym.exceptions.WorkoutNotFoundException;
import igym.services.WorkoutService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing workout creation.
 */
@RequestMapping(value = "/api")
@RestController
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /**
     * Creates a new workout via POST request.
     * 
     * @param workout the workout to create
     * @return the created workout and HTTP 201 status
     */

    @PostMapping(value = "/gyms/{gymId}/workouts", produces = "application/json")
    public ResponseEntity<Workout> createWorkout(
            @PathVariable UUID gymId,
            @RequestBody @Valid Workout workout) {
        Workout createdWorkout = workoutService.createWorkout(workout, gymId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkout);
    }

    /**
     * Soft deletes a workout and its associated exercises by marking their status
     * as {@code Status.inactive}. This does not remove the workout from the
     * database.
     *
     * <p>
     * This operation allows for logical deletion, useful for auditing and potential
     * recovery.
     * </p>
     *
     * @param workoutId the UUID of the workout to delete
     * @return {@link ResponseEntity#noContent()} if the deletion was successful
     * @throws WorkoutNotFoundException if no workout is found with the given ID
     */
    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable("id") UUID workoutId) {
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.noContent().build();
    }

}
