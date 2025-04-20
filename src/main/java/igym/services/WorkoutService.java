package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.exceptions.GymNotFoundException;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

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
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new GymNotFoundException("Gym not found"));

        workout.setGym(gym);

        if (workout.getExerciseList() != null) {
            for (Exercise exercise : workout.getExerciseList()) {
                exercise.setWorkout(workout);
            }
        }

        return workoutRepository.save(workout);
    }
}
