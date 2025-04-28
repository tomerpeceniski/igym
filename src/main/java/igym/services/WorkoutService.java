package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutService.class);

    private final WorkoutRepository workoutRepository;
    private final GymRepository gymRepository;

    public WorkoutService(WorkoutRepository workoutRepository, GymRepository gymRepository) {
        this.workoutRepository = workoutRepository;
        this.gymRepository = gymRepository;
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

    public List<Workout> getWorkoutsByGymId(UUID gymId) {
        Gym gym = findGymById(gymId);
        return workoutRepository.findByGym(gym);
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

        Workout workout = findById(workoutId);

        workout.setStatus(Status.inactive);
        logger.info("Workout with id {} marked as inactive", workoutId);

        deleteExerciesList(workout);

        workoutRepository.save(workout);
        logger.info("Workout with id {} have been inactivated", workoutId);
    }

    private void deleteExerciesList(Workout workout) {
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
}
