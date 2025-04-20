package igym.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import igym.entities.Exercise;
import igym.entities.Workout;
import igym.services.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("should return 422 Unprocessable Entity for invalid workout names")
    void testCreateWorkoutWithInvalidName(String invalidName, Set<String> expectedMessages) throws Exception {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(3);

        Workout invalidWorkout = new Workout();
        invalidWorkout.setWorkoutName(invalidName);
        invalidWorkout.setExerciseList(List.of(ex));

        MvcResult result = mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidWorkout)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        String actualMessage = json.get("errors").get(0).asText();

        assertTrue(expectedMessages.contains(actualMessage),
                "Expected one of " + expectedMessages + " but got: " + actualMessage);

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    private static Stream<Arguments> provideInvalidWorkoutNames() {
        return Stream.of(
                Arguments.of(null, Set.of("Workout name is required")),
                Arguments.of("", Set.of("Workout name is required", "Name must be between 3 and 50 characters")),
                Arguments.of("A".repeat(51), Set.of("Name must be between 3 and 50 characters")));
    }

    @Test
    @DisplayName("should return 422 Unprocessable Entity when workout has no exercises")
    void testCreateWorkoutWithEmptyExerciseList() throws Exception {
        workout.setExerciseList(List.of());

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isUnprocessableEntity());

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidExercises")
    @DisplayName("should return 422 Unprocessable Entity for invalid exercise fields")
    void testCreateWorkoutWithInvalidExercises(Exercise invalidExercise, Set<String> expectedMessages)
            throws Exception {
        Workout workout = new Workout();
        workout.setWorkoutName("Valid Workout");
        workout.setExerciseList(List.of(invalidExercise));

        MvcResult result = mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        String actualMessage = json.get("errors").get(0).asText();

        assertTrue(expectedMessages.contains(actualMessage),
                "Expected one of " + expectedMessages + " but got: " + actualMessage);

        verify(workoutService, never()).createWorkout(any(Workout.class));
    }

    private static Stream<Arguments> provideInvalidExercises() {
        return Stream.of(
                Arguments.of(
                        Exercise.builder().name("").weight(20).numReps(10).numSets(3).build(),
                        Set.of("Exercise name is required", "Name must be between 3 and 50 characters")),
                Arguments.of(
                        Exercise.builder().name("Pushup").weight(-1).numReps(10).numSets(3).build(),
                        Set.of("Weight must be zero or positive")),
                Arguments.of(
                        Exercise.builder().name("Pushup").weight(20).numReps(0).numSets(3).build(),
                        Set.of("Reps must be at least 1")),
                Arguments.of(
                        Exercise.builder().name("Pushup").weight(20).numReps(10).numSets(0).build(),
                        Set.of("Sets must be at least 1")));
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
