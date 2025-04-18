package igym.services;

import igym.entities.Exercise;
import igym.entities.Workout;
import igym.repositories.WorkoutRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    @DisplayName("Test valid workout creation")
    void testCreateWorkout() {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(4);

        Workout workout = new Workout();
        workout.setWorkoutName("Leg Day");
        workout.setExerciseList(List.of(ex));

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout);

        assertThat(result).isNotNull();
        assertThat(result.getWorkoutName()).isEqualTo("Leg Day");
        verify(workoutRepository, times(1)).save(workout);
    }
}
