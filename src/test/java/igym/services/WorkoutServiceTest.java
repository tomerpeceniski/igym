package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
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
    @DisplayName("Test soft delete of existing workout")
    void testDeleteWorkoutByIdSuccess() {
        UUID workoutId = UUID.randomUUID();

        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(4);

        Workout workout = new Workout();
        workout.setName("Leg Day");
        workout.setExerciseList(List.of(ex));
        workout.setStatus(Status.active);
        ex.setStatus(Status.active);
        ex.setWorkout(workout);

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.of(workout));

        workoutService.deleteWorkout(workoutId);

        assertEquals(Status.inactive, workout.getStatus());
        assertEquals(Status.inactive, ex.getStatus());
        verify(workoutRepository).save(workout);
    }

    @Test
    @DisplayName("Test delete workout not found throws exception")
    void testDeleteWorkoutNotFound() {
        UUID workoutId = UUID.randomUUID();

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.empty());

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.deleteWorkout(workoutId));

        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());
        verify(workoutRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when deleting workout already marked as inactive")
    void testDeleteWorkoutAlreadyInactive() {
        UUID workoutId = UUID.randomUUID();

        Workout workout = new Workout();
        workout.setName("Cardio Day");
        workout.setStatus(Status.inactive); 

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.of(workout));

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.deleteWorkout(workoutId));

        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());

        verify(workoutRepository, times(1)).findById(workoutId);
        verify(workoutRepository, never()).save(any());
        assertEquals(Status.inactive, workout.getStatus());
    }

}
