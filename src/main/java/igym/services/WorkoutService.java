package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

import java.util.List;
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

    public List<Workout> getWorkoutsByGymId(UUID gymId) {
        Gym gym = gymService.findById(gymId);
        return workoutRepository.findByGym(gym);
    }

}
