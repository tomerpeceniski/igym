package igym.services;

import igym.entities.Exercise;
import igym.entities.Workout;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    /**
     * Creates a new workout with its list of exercises.
     * 
     * @param workout the workout entity to save
     * @return the saved workout
     */
    @Transactional
    public Workout createWorkout(Workout workout) {
        for (Exercise exercise : workout.getExerciseList()) {
            exercise.setWorkout(workout); 
        }
        return workoutRepository.save(workout);
    }
}
