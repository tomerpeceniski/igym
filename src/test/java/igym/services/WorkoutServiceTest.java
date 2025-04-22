package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.exceptions.GymNotFoundException;
import igym.repositories.WorkoutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private GymService gymService;

    @InjectMocks
    private WorkoutService workoutService;

    UUID gymId = UUID.randomUUID();

    @Test
    @DisplayName("Test valid workout creation")
    void testCreateWorkout() {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(4);

        Workout workout = new Workout();
        workout.setName("Leg Day");
        workout.setExerciseList(List.of(ex));

        when(gymService.findById(gymId)).thenReturn(new Gym("Test Gym"));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout, gymId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Leg Day");
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    @DisplayName("Should return an exeception when gym not found in workout creation")
    void testGymNotFoundInWorkoutCreation() {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(4);

        Workout workout = new Workout();
        workout.setName("Leg Day");
        workout.setExerciseList(List.of(ex));

        when(gymService.findById(gymId)).thenThrow(new GymNotFoundException("Gym with id " + gymId + " not found"));

        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> workoutService.createWorkout(workout, gymId));

        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
        verify(gymService, times(1)).findById(gymId);
        verify(workoutRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return multiple workouts by gym ID")
    void testGetMultipleWorkoutsByGymId() {
        Gym gym = new Gym("My Gym");
        UUID gymId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Pushup");
        ex1.setWeight(0);
        ex1.setNumReps(15);
        ex1.setNumSets(3);

        Exercise ex2 = new Exercise();
        ex2.setName("Squat");
        ex2.setWeight(40);
        ex2.setNumReps(10);
        ex2.setNumSets(4);

        Workout workout1 = new Workout();
        workout1.setName("Upper Body");
        workout1.setExerciseList(List.of(ex1));
        workout1.setGym(gym);

        Workout workout2 = new Workout();
        workout2.setName("Leg Day");
        workout2.setExerciseList(List.of(ex2));
        workout2.setGym(gym);

        when(gymService.findById(gymId)).thenReturn(gym);
        when(workoutRepository.findByGym(gym)).thenReturn(List.of(workout1, workout2));

        List<Workout> workouts = workoutService.getWorkoutsByGymId(gymId);

        assertEquals(2, workouts.size());
        assertEquals("Upper Body", workouts.get(0).getName());
        assertEquals("Leg Day", workouts.get(1).getName());

        verify(gymService, times(1)).findById(gymId);
        verify(workoutRepository, times(1)).findByGym(gym);
    }

    @Test
    @DisplayName("Should throw GymNotFoundException when gym ID does not exist in getWorkoutsByGymId")
    void testGetWorkoutsByGymIdGymNotFound() {
        UUID gymId = UUID.randomUUID();

        when(gymService.findById(gymId))
                .thenThrow(new GymNotFoundException("Gym with id " + gymId + " not found"));

        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> workoutService.getWorkoutsByGymId(gymId));

        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
        verify(gymService, times(1)).findById(gymId);
        verify(workoutRepository, never()).findByGym(any());
    }

}
