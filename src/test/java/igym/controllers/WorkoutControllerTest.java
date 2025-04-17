package igym.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import igym.entities.Exercise;
import igym.entities.Workout;
import igym.services.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
@ExtendWith(MockitoExtension.class)
public class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private WorkoutService workoutService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Workout workout;

    @BeforeEach
    void setUp() {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(3);

        workout = new Workout();
        workout.setWorkoutName("Leg Day");
        workout.setExerciseList(List.of(ex));

        ReflectionTestUtils.setField(workout, "id", UUID.randomUUID());
    }

    @Test
    @DisplayName("should return status 201 when a valid workout is created")
    void testCreateWorkoutSuccess() throws Exception {
        when(workoutService.createWorkout(any(Workout.class))).thenReturn(workout);

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.workoutName").value(workout.getWorkoutName()))
                .andExpect(jsonPath("$.exerciseList[0].name").value("Squat"));

        verify(workoutService, times(1)).createWorkout(any(Workout.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidWorkoutNames")
    @DisplayName("should return 400 Bad Request for invalid workout names")
    void testCreateWorkoutWithInvalidName(String invalidName) throws Exception {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(3);

        Workout invalidWorkout = new Workout();
        invalidWorkout.setWorkoutName(invalidName);
        invalidWorkout.setExerciseList(List.of(ex));

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidWorkout)))
                .andExpect(status().isBadRequest());

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    private static Stream<String> provideInvalidWorkoutNames() {
        return Stream.of(null, "", "  ", "AB", "A".repeat(51));
    }

    @Test
    @DisplayName("should return 400 Bad Request when workout has no exercises")
    void testCreateWorkoutWithEmptyExerciseList() throws Exception {
        workout.setExerciseList(List.of());

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isBadRequest());

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    @Test
    @DisplayName("should return 400 Bad Request when exercise has invalid fields")
    void testCreateWorkoutWithInvalidExercise() throws Exception {
        Exercise ex = new Exercise();
        ex.setName("");
        ex.setWeight(-5);
        ex.setNumReps(0);
        ex.setNumSets(0);

        workout.setExerciseList(List.of(ex));

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isBadRequest());

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    @Test
    @DisplayName("should return 500 Internal Server Error when service throws unexpected exception")
    void testCreateWorkoutInternalError() throws Exception {
        when(workoutService.createWorkout(any(Workout.class)))
                .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isInternalServerError());

        verify(workoutService, times(1)).createWorkout(any(Workout.class));
    }

}
