package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
import igym.exceptions.ExerciseNotFoundException;
import igym.repositories.ExerciseRepository;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final GymRepository gymRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutService(WorkoutRepository workoutRepository, GymRepository gymRepository,
            ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.gymRepository = gymRepository;
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

        Gym gym = findGymById(gymId);
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
        logger.info("Fetching Workouts for Gym with ID {}", gymId);
        Gym gym = findGymById(gymId);
        List<Workout> workouts = workoutRepository.findByGymAndStatus(gym, Status.active);

        workouts.forEach(workout -> {
            if (workout.getExerciseList() != null) {
                List<Exercise> activeExercises = workout.getExerciseList().stream()
                        .filter(e -> e.getStatus() == Status.active)
                        .toList();
                workout.setExerciseList(activeExercises);
            }
        });
        logger.info("Found {} active workouts for gym {}", workouts.size(), gymId);
        return workouts;
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
        logger.info("Attempting to inactivate workout with id: {}", workoutId);
        Workout workout = findByIdAndStatus(workoutId, Status.active);
        workout.setStatus(Status.inactive);

        deleteExercisesList(workout);

        workoutRepository.save(workout);
        logger.info("Workout with id {} inactivated", workoutId);
    }

    private void deleteExercisesList(Workout workout) {
        List<Exercise> exercises = workout.getExerciseList();
        if (exercises != null) {
            exercises.forEach(e -> {
                if (e.getStatus() == Status.active) {
                    e.setStatus(Status.inactive);
                    logger.info("Exercise {} inactivated", e.getId());
                }
            });
        }
    }

    /**
     * Soft deletes an exercise by marking its status as {@code Status.inactive}.
     *
     * @param exerciseId the UUID of the exercise to soft delete
     * @throws WorkoutNotFoundException if the workout or exercise with the provided
     *                                  IDs do not exist or are already inactive
     */
    @Transactional
    public void deleteExercise(UUID exerciseId) {
        logger.info("Attempting to inactivate exercise with id: {}", exerciseId);

        Exercise exercise = exerciseRepository.findByIdAndStatus(exerciseId, Status.active)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise with id " + exerciseId + " not found"));

        exercise.setStatus(Status.inactive);
        logger.info("Exercise with id {} has been inactivated", exerciseId);
    }

    /**
     * Fetch Workout by its ID, ensuring it is active.
     *
     * @param id the UUID of the workout to be fetched
     * @return the found workout
     * @throws WorkoutNotFoundException if the workout with the provided ID does not
     *                                  exist or is inactive
     */
    public Workout findById(UUID id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Workout with id {} not found", id);
                    return new WorkoutNotFoundException("Workout with id " + id + " not found");
                });

        if (workout.getStatus() == Status.inactive) {
            logger.warn("Workout with id {} is inactive", id);
            throw new WorkoutNotFoundException("Workout with id " + id + " not found");
        }

        return workout;
    }

    private Gym findGymById(UUID gymId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + gymId + " not found"));
        if (gym.getStatus() == Status.inactive) {
            logger.warn("Gym with id {} is inactive", gymId);
            throw new GymNotFoundException("Gym with id " + gymId + " not found");
        }
        return gym;
    }

    /**
     * Updates workout details.
     * This method updates the workout's name and the exercise list.
     */
    @Transactional
    public Workout updateWorkout(UUID workoutId, Workout updatedWorkout) {
        logger.info("Attempting to update workout with id: {}", workoutId);
        logger.debug("Update request with values: {}", updatedWorkout);
        Workout existingWorkout = findByIdAndStatus(workoutId, Status.active);
        existingWorkout.setName(updatedWorkout.getName());

        List<Exercise> updatedExercises = updatedWorkout.getExerciseList() != null
                ? updatedWorkout.getExerciseList()
                : new ArrayList<>();

        updatedExercises.forEach(ex -> {
            ex.setWorkout(existingWorkout);
            ex.setStatus(Status.active);
        });

        existingWorkout.setExerciseList(updatedExercises);
        existingWorkout.setStatus(Status.active);

        Workout savedWorkout = workoutRepository.save(existingWorkout);
        logger.info("Workout with id {} updated successfully", savedWorkout.getId());
        logger.debug("Updated workout details: {}", savedWorkout);
        return savedWorkout;
    }

    private Workout findByIdAndStatus(UUID id, Status status) {
        logger.info("Fetching workout with id: {} and status: {}", id, status);
        Workout workout = workoutRepository.findByIdAndStatus(id, status)
                .orElseThrow(() -> {
                    logger.warn("Workout with id {} and status {} not found", id, status);
                    return new WorkoutNotFoundException("Workout with id " + id + " not found");
                });
        return workout;

    }
}
