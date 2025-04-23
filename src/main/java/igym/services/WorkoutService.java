package igym.services;

import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.WorkoutNotFoundException;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkoutService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutService.class);

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
        logger.info("Attempting to create a new workout for gym with id: {}", gymId);
        logger.debug("Workout creation request with values: {}", workout);

        Gym gym = gymService.findById(gymId);
        workout.setGym(gym);

        if (workout.getExerciseList() != null) {
            workout.getExerciseList().forEach(exercise -> {
                exercise.setWorkout(workout);
                logger.debug("Setting workout reference for exercise: {}", exercise);
            });
        }

        Workout savedWorkout = workoutRepository.save(workout);
        logger.info("New workout created with id: {}", savedWorkout.getId());
        logger.debug("New workout persisted: {}", savedWorkout);
        return savedWorkout;
    }

    /**
     * Soft deletes a workout by marking its status (and the status of its
     * exercises) as {@code Status.inactive}.
     *
     * @param workoutId the UUID of the workout to soft delete
     */
    @Transactional
    public void deleteWorkout(UUID workoutId) {
        logger.info("Attempting to delete workout with id: {}", workoutId);

        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> {
                    logger.warn("Workout with id {} not found", workoutId);
                    return new WorkoutNotFoundException("Workout with id " + workoutId + " not found");
                });

        if (workout.getStatus() == Status.inactive) {
            logger.warn("Workout with id {} is already inactive", workoutId);
            throw new WorkoutNotFoundException("Workout with id " + workoutId + " not found");
        }

        workout.setStatus(Status.inactive);
        logger.info("Workout with id {} marked as inactive", workoutId);

        if (workout.getExerciseList() != null) {
            workout.getExerciseList().forEach(ex -> {
                ex.setStatus(Status.inactive);
                logger.debug("Exercise with id {} marked as inactive", ex.getId());
            });
        }

        workoutRepository.save(workout);
        logger.info("Workout with id {} and its exercises have been inactivated", workoutId);
    }
}