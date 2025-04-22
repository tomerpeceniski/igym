package igym.controllers;

import igym.entities.Workout;
import igym.services.WorkoutService;
import jakarta.validation.Valid;

import java.util.List;
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
     * Retrieves all workouts for a specific gym via GET request.
     * 
     * @param gymId the ID of the gym
     * @return a list of workouts and HTTP 200 status
     */
    @GetMapping(value = "/gyms/{gymId}/workouts", produces = "application/json")
    public ResponseEntity<List<Workout>> getWorkoutsByGymId(@PathVariable UUID gymId) {
        List<Workout> workouts = workoutService.getWorkoutsByGymId(gymId);
        return ResponseEntity.ok(workouts);
    }

}