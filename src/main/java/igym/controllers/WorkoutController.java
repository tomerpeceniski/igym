package igym.controllers;

import igym.dtos.WorkoutDTO;
import igym.entities.Workout;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
import igym.services.WorkoutService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing workouts.
 * Provides endpoints for creating, retrieving, updating, and deleting workouts.
 */
@RequestMapping(value = "/api")
@RestController
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /**
     * Creates a new workout associated with a specific gym via POST request.
     *
     * @param gymId   the UUID of the gym where the workout will be created
     * @param workout the workout entity to be created
     * @return the created workout (as DTO) with HTTP 201 Created status
     * @throws GymNotFoundException if no gym is found with the provided ID
     */
    @PostMapping(value = "/gyms/{gymId}/workouts", produces = "application/json")
    public ResponseEntity<WorkoutDTO> createWorkout(
            @PathVariable UUID gymId,
            @RequestBody @Valid Workout workout) {
        Workout createdWorkout = workoutService.createWorkout(workout, gymId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new WorkoutDTO(createdWorkout));
    }

    /**
     * Retrieves all workouts for a specific gym via GET request.
     * 
     * @param gymId the ID of the gym
     * @return a list of workouts (as DTO) and HTTP 200 status
     * @throws GymNotFoundException if no gym is found with the provided ID
     */
    @GetMapping(value = "/gyms/{gymId}/workouts", produces = "application/json")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsByGymId(@PathVariable UUID gymId) {
        List<WorkoutDTO> workouts = workoutService.getWorkoutsByGymId(gymId).stream().map(WorkoutDTO::new).toList();
        return ResponseEntity.ok(workouts);
    }

    /**
     * Soft deletes a workout and its associated exercises by marking their status
     * as {@code Status.inactive}. This does not remove the workout from the
     * database.
     *
     * This operation allows for logical deletion, useful for auditing and potential
     * recovery.
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

    /**
     * Retrieves a updated workout by its ID.
     * 
     * @param workoutId the ID of the workout to retrieve
     * @return the workout with the specified ID (as DTO) and HTTP 200 status
     */
    @PatchMapping(value = "/workouts/{id}", produces = "application/json")
    public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable("id") UUID workoutId,
            @RequestBody @Valid Workout workout) {
        Workout updatedWorkout = workoutService.updateWorkout(workoutId, workout);
        return ResponseEntity.ok(new WorkoutDTO(updatedWorkout));
    }

}
