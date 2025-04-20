package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private GymRepository gymRepository;

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

        when(gymRepository.findById(gymId)).thenReturn(java.util.Optional.of(new Gym("Test Gym")));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout, gymId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Leg Day");
verify(workoutRepository, times(1)).save(any(Workout.class));
    }
}
