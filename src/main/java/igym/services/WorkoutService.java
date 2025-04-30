package igym.services;

import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing workout operations,
 * handling business logic, and interacting with the repository layer.
 *
 * <p>
 * This service receives requests from the controller layer, applies necessary
 * validations
 * and transformations, and persists or retrieves workout-related data.
 * </p>
 */

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
     * Creates a new workout associated with a specific gym.
     *
     * <p>
     * Also links each exercise to the created workout entity.
     * </p>
     *
     * @param workout the workout entity to be saved
     * @param gymId   the UUID of the gym to associate the workout with
     * @return the saved workout entity
     * @throws GymNotFoundException if the gym with the provided ID does not exist
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
     * Retrieves all workouts associated with a specific gym.
     *
     * @param gymId the UUID of the gym
     * @return a list of workouts belonging to the specified gym
     * @throws GymNotFoundException if the gym with the provided ID does not exist
     *                              or is inactive
     */
    public List<Workout> getWorkoutsByGymId(UUID gymId) {
        Gym gym = gymService.findById(gymId);
        return workoutRepository.findByGym(gym);
    }

    /**
     * Soft deletes a workout by marking its status and the statuses of its
     * exercises as {@code Status.inactive}.
     *
     * @param workoutId the UUID of the workout to soft delete
     * @throws WorkoutNotFoundException if the workout with the provided ID does not
     *                                  exist or is already inactive
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
