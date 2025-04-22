package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.WorkoutNotFoundException;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final GymService gymService;

    public WorkoutService(WorkoutRepository workoutRepository, GymService gymService) {
        this.workoutRepository = workoutRepository;
        this.gymService = gymService;
    }

    /**
     * Creates a new workout with its list of exercises.
     * 
     * @param workout the workout entity to save
     * @return the saved workout
     */
    @Transactional
    public Workout createWorkout(Workout workout, UUID gymId) {
        Gym gym = gymService.findById(gymId);
        workout.setGym(gym);

        if (workout.getExerciseList() != null) {
            for (Exercise exercise : workout.getExerciseList()) {
                exercise.setWorkout(workout);
            }
        }

        return workoutRepository.save(workout);
    }

    /**
     * Soft deletes a workout by marking its status (and the status of its
     * exercises)
     * as {@code Status.inactive}. This prevents the workout and its exercises from
     * being
     * returned in future queries unless explicitly requested.
     *
     * <p>
     * This method throws a {@link igym.exceptions.WorkoutNotFoundException}
     * if no workout with the given ID exists.
     *
     * <p>
     * Note: The workout is still persisted in the database with status
     * {@code inactive}, allowing for potential recovery or auditing.
     *
     * @param workoutId the UUID of the workout to soft delete
     * @throws igym.exceptions.WorkoutNotFoundException if no workout is found with
     *                                                  the given ID
     */
    @Transactional
    public void deleteWorkout(UUID workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout with id " + workoutId + " not found"));

        if (workout.getStatus() == Status.inactive) {
            throw new WorkoutNotFoundException("Workout with id " + workoutId + " not found");
        }

        workout.setStatus(Status.inactive);

        if (workout.getExerciseList() != null) {
            workout.getExerciseList().forEach(ex -> ex.setStatus(Status.inactive));
        }

        workoutRepository.save(workout);
    }
}
