package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.ExerciseNotFoundException;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.WorkoutNotFoundException;
import igym.repositories.ExerciseRepository;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private ExerciseRepository exerciseRepository;

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

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(new Gym("Test Gym")));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout, gymId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Leg Day");
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    @DisplayName("Should create workout when exercise list is null")
    void testCreateWorkoutWithNullExerciseList() {
        Workout workout = new Workout();
        workout.setName("Stretching Routine");
        workout.setExerciseList(null);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(new Gym("Test Gym")));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workout, gymId);

        assertEquals("Stretching Routine", result.getName());
        verify(workoutRepository, times(1)).save(workout);
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

        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());

        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> workoutService.createWorkout(workout, gymId));

        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
        verify(gymRepository, times(1)).findById(gymId);
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

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(workoutRepository.findByGymAndStatus(gym, Status.active)).thenReturn(List.of(workout1, workout2));

        List<Workout> workouts = workoutService.getWorkoutsByGymId(gymId);

        assertEquals(2, workouts.size());
        assertEquals("Upper Body", workouts.get(0).getName());
        assertEquals("Leg Day", workouts.get(1).getName());

        verify(gymRepository, times(1)).findById(gymId);
        verify(workoutRepository, times(1)).findByGymAndStatus(gym, Status.active);
    }

    @Test
    @DisplayName("Should return empty list of workouts when the workouts are inactive")
    void testGetWorkoutsByGymIdInactive() {
        Gym gym = new Gym("My Gym");
        UUID gymId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Pushup");
        ex1.setWeight(0);
        ex1.setNumReps(15);
        ex1.setNumSets(3);
        ex1.setStatus(Status.inactive);

        Workout workout1 = new Workout();
        workout1.setName("Upper Body");
        workout1.setExerciseList(List.of(ex1));
        workout1.setGym(gym);
        workout1.setStatus(Status.inactive);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(workoutRepository.findByGymAndStatus(gym, Status.active)).thenReturn(List.of());

        List<Workout> workouts = workoutService.getWorkoutsByGymId(gymId);

        assertEquals(0, workouts.size());
        verify(gymRepository, times(1)).findById(gymId);
        verify(workoutRepository, times(1)).findByGymAndStatus(gym, Status.active);
    }

    @Test
    @DisplayName("Should return not return inactive exercises when workout is active in getWorkoutsByGymId")
    void testGetWorkoutsByGymIdInactiveExercises() {
        Gym gym = new Gym("My Gym");
        UUID gymId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Pushup");
        ex1.setWeight(0);
        ex1.setNumReps(15);
        ex1.setNumSets(3);
        ex1.setStatus(Status.inactive);

        Workout workout1 = new Workout();
        workout1.setName("Upper Body");
        workout1.setExerciseList(List.of(ex1));
        workout1.setGym(gym);
        workout1.setStatus(Status.active);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(workoutRepository.findByGymAndStatus(gym, Status.active)).thenReturn(List.of(workout1));

        List<Workout> workouts = workoutService.getWorkoutsByGymId(gymId);

        assertEquals(1, workouts.size());
        assertEquals("Upper Body", workouts.get(0).getName());
        assertEquals(0, workouts.get(0).getExerciseList().size());

        verify(gymRepository, times(1)).findById(gymId);
        verify(workoutRepository, times(1)).findByGymAndStatus(gym, Status.active);
    }

    @Test
    @DisplayName("Should throw GymNotFoundException when gym ID does not exist in getWorkoutsByGymId")
    void testGetWorkoutsByGymIdGymNotFound() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("My Gym");

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());

        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> workoutService.getWorkoutsByGymId(gymId));

        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
        verify(gymRepository, times(1)).findById(gymId);
        verify(workoutRepository, never()).findByGymAndStatus(gym, Status.active);

    }

    @DisplayName("Test soft delete of existing workout")
    void testDeleteWorkoutByIdSuccess() {
        UUID workoutId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Squat");
        ex1.setWeight(60);
        ex1.setNumReps(10);
        ex1.setNumSets(4);

        Exercise ex2 = new Exercise();
        ex2.setName("Plank");
        ex2.setWeight(60);
        ex2.setNumReps(10);
        ex2.setNumSets(4);

        Workout workout = new Workout();
        workout.setName("Leg Day");
        workout.setExerciseList(List.of(ex1, ex2));
        workout.setStatus(Status.active);
        ex1.setStatus(Status.active);
        ex2.setStatus(Status.inactive);
        ex1.setWorkout(workout);

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.of(workout));

        workoutService.deleteWorkout(workoutId);

        assertEquals(Status.inactive, workout.getStatus());
        assertEquals(Status.inactive, ex1.getStatus());
        assertEquals(Status.inactive, ex2.getStatus());
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

        when(workoutRepository.findByIdAndStatus(workoutId, Status.inactive)).thenReturn(java.util.Optional.of(workout));

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.deleteWorkout(workoutId));

        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());

        verify(workoutRepository, never()).save(any());
        assertEquals(Status.inactive, workout.getStatus());
    }

    @Test
    @DisplayName("Should delete workout with no exercises")
    void testDeleteWorkoutWithNoExercises() {
        UUID workoutId = UUID.randomUUID();

        Workout workout = new Workout();
        workout.setName("Cardio Day");
        workout.setStatus(Status.active);
        workout.setExerciseList(null);

        when(workoutRepository.findByIdAndStatus(workoutId, Status.active)).thenReturn(java.util.Optional.of(workout));

        workoutService.deleteWorkout(workoutId);

        assertEquals(Status.inactive, workout.getStatus());
        verify(workoutRepository).save(workout);
    }

    @Test
    @DisplayName("Test soft delete of existing exercise in workout")
    void testDeleteExerciseByIdSuccess() {
        UUID exerciseId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Squat");
        ex1.setWeight(60);
        ex1.setNumReps(10);
        ex1.setNumSets(4);
        ex1.setStatus(Status.active);
        ReflectionTestUtils.setField(ex1, "id", exerciseId);

        when(exerciseRepository.findByIdAndStatus(exerciseId, Status.active)).thenReturn(java.util.Optional.of(ex1));

        workoutService.deleteExercise(exerciseId);

        assertEquals(Status.inactive, ex1.getStatus());
    }

    @Test
    @DisplayName("Test succesfully update workout and its exercises")
    void testUpdateWorkout() {
        UUID workoutId = UUID.randomUUID();
        Workout existingWorkout = new Workout();
        Exercise ex1 = new Exercise();
        existingWorkout.setName("Leg Day");
        existingWorkout.setExerciseList(new ArrayList<>(List.of(ex1)));
        existingWorkout.setStatus(Status.active);

        when(workoutRepository.findByIdAndStatus(workoutId, Status.active)).thenReturn(java.util.Optional.of(existingWorkout));
        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exercise ex2 = new Exercise();
        ex2.setName("Deadlift");
        ex2.setWeight(80);
        ex2.setNumReps(8);
        ex2.setNumSets(3);

        Workout updatedWorkout = new Workout();
        updatedWorkout.setName("Full Body Day");
        updatedWorkout.setExerciseList(new ArrayList<>(List.of(ex2)));

        workoutService.updateWorkout(workoutId, updatedWorkout);

        assertEquals("Full Body Day", existingWorkout.getName());
        assertEquals(1, existingWorkout.getExerciseList().size());
        assertEquals("Deadlift", existingWorkout.getExerciseList().get(0).getName());
        assertEquals(Status.active, existingWorkout.getStatus());

    }

    @Test
    @DisplayName("Test update workout not found throws exception")
    void testUpdateWorkoutNotFound() {
        UUID workoutId = UUID.randomUUID();

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.empty());

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.updateWorkout(workoutId, new Workout()));

        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());
        verify(workoutRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test update workout with null exercise list")
    void testUpdateWorkoutWithNullExerciseList() {
        UUID workoutId = UUID.randomUUID();

        Workout existingWorkout = new Workout();
        existingWorkout.setName("Leg Day");
        existingWorkout.setExerciseList(new ArrayList<>());
        existingWorkout.setStatus(Status.active);

        when(workoutRepository.findByIdAndStatus(workoutId, Status.active)).thenReturn(java.util.Optional.of(existingWorkout));
        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Workout updatedWorkout = new Workout();
        updatedWorkout.setName("Leg Day Updated");
        updatedWorkout.setExerciseList(null);

        workoutService.updateWorkout(workoutId, updatedWorkout);

        assertEquals("Leg Day Updated", existingWorkout.getName());
        assertEquals(0, existingWorkout.getExerciseList().size());
        assertEquals(Status.active, existingWorkout.getStatus());
        verify(workoutRepository).save(existingWorkout);
    }

    @Test
    @DisplayName("Test update inactive workout throws exception")
    void testUpdateInactiveWorkout() {
        UUID workoutId = UUID.randomUUID();

        Workout existingWorkout = new Workout();
        existingWorkout.setName("Leg Day");
        existingWorkout.setExerciseList(new ArrayList<>());
        existingWorkout.setStatus(Status.inactive);

        when(workoutRepository.findById(workoutId)).thenReturn(java.util.Optional.of(existingWorkout));

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.updateWorkout(workoutId, new Workout()));

        assertEquals("Workout with id " + workoutId + " not found", exception.getMessage());
        verify(workoutRepository, never()).save(any());
        assertEquals(Status.inactive, existingWorkout.getStatus());
    }


    @Test
    @DisplayName("Test delete exercise when exercise not found in workout throws exception")
    void testDeleteExerciseNotFoundInWorkout() {
        UUID exerciseId = UUID.randomUUID();
        UUID nonExistentExerciseId = UUID.randomUUID();
        Exercise ex1 = new Exercise();
        ex1.setName("Squat");
        ex1.setWeight(60);
        ex1.setNumReps(10);
        ex1.setNumSets(4);
        ex1.setStatus(Status.active);
        ReflectionTestUtils.setField(ex1, "id", exerciseId);

        when(exerciseRepository.findByIdAndStatus(nonExistentExerciseId, Status.active)).thenReturn(Optional.empty());

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class,
                () -> workoutService.deleteExercise(nonExistentExerciseId));

        assertEquals("Exercise with id " + nonExistentExerciseId + " not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test delete exercise when exercise already inactive throws exception")
    void testDeleteExerciseAlreadyInactive() {
        UUID exerciseId = UUID.randomUUID();

        Exercise ex1 = new Exercise();
        ex1.setName("Squat");
        ex1.setWeight(60);
        ex1.setNumReps(10);
        ex1.setNumSets(4);
        ex1.setStatus(Status.inactive);
        ReflectionTestUtils.setField(ex1, "id", exerciseId);

        when(exerciseRepository.findByIdAndStatus(exerciseId, Status.active)).thenReturn(Optional.empty());

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class,
                () -> workoutService.deleteExercise(exerciseId));

        assertEquals("Exercise with id " + exerciseId + " not found", exception.getMessage());
        assertEquals(Status.inactive, ex1.getStatus());
    }


    @Test
    @DisplayName("Test update workout and its exercises trying to set the status to inactive should return a workout and it's exercises with status active")
    void testUpdateWorkoutAndItsExercisesTryingToSetStatusToInactive() {
        UUID workoutId = UUID.randomUUID();
        Exercise ex1 = new Exercise();
        Workout existingWorkout = new Workout();
        existingWorkout.setName("Leg Day");
        existingWorkout.setExerciseList(new ArrayList<>(List.of(ex1)));
        existingWorkout.setStatus(Status.active);

        when(workoutRepository.findByIdAndStatus(workoutId, Status.active)).thenReturn(java.util.Optional.of(existingWorkout));
        when(workoutRepository.save(any(Workout.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exercise ex2 = new Exercise();
        ex2.setName("Deadlift");
        ex2.setWeight(80);
        ex2.setNumReps(8);
        ex2.setNumSets(3);

        Workout updatedWorkout = new Workout();
        updatedWorkout.setName("Full Body Day");
        updatedWorkout.setExerciseList(new ArrayList<>(List.of(ex2)));
        updatedWorkout.setStatus(Status.inactive);
        updatedWorkout.getExerciseList().forEach(ex -> ex.setStatus(Status.inactive));

        workoutService.updateWorkout(workoutId, updatedWorkout);

        assertEquals("Full Body Day", existingWorkout.getName());
        assertEquals(1, existingWorkout.getExerciseList().size());
        assertEquals("Deadlift", existingWorkout.getExerciseList().get(0).getName());
        assertEquals(Status.active, existingWorkout.getStatus());
        assertEquals(Status.active, existingWorkout.getExerciseList().get(0).getStatus());
    }
}
