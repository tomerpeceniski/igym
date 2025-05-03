package igym.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.Workout;
import igym.exceptions.WorkoutNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Workout workout;
        UUID gymId;
        Gym gym;

        @BeforeEach
        void setUp() {
                gym = new Gym("Mocked Gym");
                gymId = UUID.randomUUID();
                ReflectionTestUtils.setField(gym, "id", gymId);

                Exercise ex = new Exercise();
                ReflectionTestUtils.setField(ex, "id", UUID.randomUUID());
                ex.setName("Squat");
                ex.setWeight(60);
                ex.setNumReps(10);
                ex.setNumSets(3);

                workout = new Workout();
                ReflectionTestUtils.setField(workout, "id", UUID.randomUUID());
                workout.setName("Leg Day");
                workout.setExerciseList(List.of(ex));
                workout.setGym(gym);
                ex.setWorkout(workout);
        }

        @Test
        @DisplayName("should return status 201 when a valid workout is created")
        void testCreateWorkoutSuccess() throws Exception {
                when(workoutService.createWorkout(any(Workout.class), eq(gymId))).thenReturn(workout);

                mockMvc.perform(post("/api/gyms/{gymId}/workouts", gymId, gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value(workout.getName()))
                                .andExpect(jsonPath("$.exerciseList[0].name").value("Squat"));

                verify(workoutService, times(1)).createWorkout(any(Workout.class), eq(gymId));
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
                invalidWorkout.setName(invalidName);
                invalidWorkout.setExerciseList(List.of(ex));

                MvcResult result = mockMvc.perform(post("/api/gyms/{gymId}/workouts", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidWorkout)))
                                .andExpect(status().isUnprocessableEntity())
                                .andReturn();

                String responseBody = result.getResponse().getContentAsString();
                JsonNode json = objectMapper.readTree(responseBody);
                String actualMessage = json.get("errors").get(0).asText();

                assertTrue(expectedMessages.contains(actualMessage),
                                "Expected one of " + expectedMessages + " but got: " + actualMessage);

                verify(workoutService, never()).createWorkout(any(Workout.class), eq(gymId));
        }

        private static Stream<Arguments> provideInvalidWorkoutNames() {
                return Stream.of(
                                Arguments.of(null, Set.of("Workout name is required")),
                                Arguments.of("", Set.of("Workout name is required",
                                                "Name must be between 3 and 50 characters")),
                                Arguments.of("A".repeat(51), Set.of("Name must be between 3 and 50 characters")));
        }

        @Test
        @DisplayName("should return 422 Unprocessable Entity when workout has no exercises")
        void testCreateWorkoutWithEmptyExerciseList() throws Exception {
                workout.setExerciseList(List.of());

                mockMvc.perform(post("/api/gyms/{gymId}/workouts", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout)))
                                .andExpect(status().isUnprocessableEntity());

                verify(workoutService, never()).createWorkout(any(Workout.class), eq(gymId));
        }

        @ParameterizedTest
        @MethodSource("provideInvalidExercises")
        @DisplayName("should return 422 Unprocessable Entity for invalid exercise fields")
        void testCreateWorkoutWithInvalidExercises(Exercise invalidExercise, Set<String> expectedMessages)
                        throws Exception {
                Workout workout = new Workout();
                workout.setName("Valid Workout");
                workout.setExerciseList(List.of(invalidExercise));

                MvcResult result = mockMvc.perform(post("/api/gyms/{gymId}/workouts", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout)))
                                .andExpect(status().isUnprocessableEntity())
                                .andReturn();

                String responseBody = result.getResponse().getContentAsString();
                JsonNode json = objectMapper.readTree(responseBody);
                String actualMessage = json.get("errors").get(0).asText();

                assertTrue(expectedMessages.contains(actualMessage),
                                "Expected one of " + expectedMessages + " but got: " + actualMessage);

                verify(workoutService, never()).createWorkout(any(Workout.class), eq(gymId));
        }

        private static Stream<Arguments> provideInvalidExercises() {
                return Stream.of(
                                Arguments.of(
                                                createExercise("", 20, 10, 3),
                                                Set.of("Exercise name is required",
                                                                "Name must be between 3 and 50 characters")),
                                Arguments.of(
                                                createExercise("Pushup", -1, 10, 3),
                                                Set.of("Weight must be zero or positive")),
                                Arguments.of(
                                                createExercise("Pushup", 20, 0, 3),
                                                Set.of("Reps must be at least 1")),
                                Arguments.of(
                                                createExercise("Pushup", 20, 10, 0),
                                                Set.of("Sets must be at least 1")));
        }

        private static Exercise createExercise(String name, double weight, int reps, int sets) {
                Exercise ex = new Exercise();
                ex.setName(name);
                ex.setWeight(weight);
                ex.setNumReps(reps);
                ex.setNumSets(sets);
                return ex;
        }

        @Test
        @DisplayName("should return 500 Internal Server Error when service throws unexpected exception")
        void testCreateWorkoutInternalError() throws Exception {
                when(workoutService.createWorkout(any(Workout.class), eq(gymId)))
                                .thenThrow(new RuntimeException("Unexpected failure"));

                mockMvc.perform(post("/api/gyms/{gymId}/workouts", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout)))
                                .andExpect(status().isInternalServerError());

                verify(workoutService, times(1)).createWorkout(any(Workout.class), eq(gymId));
        }

        @Test
        @DisplayName("should return all workouts for a given gym")
        void testGetWorkoutsByGymId() throws Exception {
                Exercise ex2 = new Exercise();
                ReflectionTestUtils.setField(ex2, "id", UUID.randomUUID());
                ex2.setName("Pushup");
                ex2.setWeight(0);
                ex2.setNumReps(15);
                ex2.setNumSets(3);

                Workout workout2 = new Workout();
                ReflectionTestUtils.setField(workout2, "id", UUID.randomUUID());
                workout2.setName("Upper Body");
                workout2.setExerciseList(List.of(ex2));
                workout2.setGym(gym);
                ex2.setWorkout(workout2);

                when(workoutService.getWorkoutsByGymId(gymId)).thenReturn(List.of(workout, workout2));

                mockMvc.perform(get("/api/gyms/{gymId}/workouts", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("Leg Day"))
                                .andExpect(jsonPath("$[1].name").value("Upper Body"));

                verify(workoutService, times(1)).getWorkoutsByGymId(gymId);

        }

        @DisplayName("should return 204 No Content when deleting existing workout")
        void testDeleteWorkoutSuccess() throws Exception {
                UUID workoutId = UUID.randomUUID();

                doNothing().when(workoutService).deleteWorkout(workoutId);

                mockMvc.perform(delete("/api/workouts/{id}", workoutId))
                                .andExpect(status().isNoContent());

                verify(workoutService, times(1)).deleteWorkout(workoutId);
        }

        @Test
        @DisplayName("should return 404 Not Found when workout does not exist")
        void testDeleteWorkoutNotFound() throws Exception {
                UUID workoutId = UUID.randomUUID();

                doThrow(new WorkoutNotFoundException("Workout not found")).when(workoutService)
                                .deleteWorkout(workoutId);

                mockMvc.perform(delete("/api/workouts/{id}", workoutId))
                                .andExpect(status().isNotFound());

                verify(workoutService, times(1)).deleteWorkout(workoutId);
        }

        @Test
        @DisplayName("shoudl return 200 OK when updating a workout and its exercises")
        void testUpdateWorkout() throws Exception {
                UUID workoutId = UUID.randomUUID();

                when(workoutService.updateWorkout(eq(workoutId), any(Workout.class))).thenReturn(workout);

                mockMvc.perform(patch("/api/workouts/{id}", workoutId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(workout)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(workout.getName()))
                                .andExpect(jsonPath("$.exerciseList[0].name").value("Squat"));

                verify(workoutService, times(1)).updateWorkout(eq(workoutId), any(Workout.class));
        }

}
