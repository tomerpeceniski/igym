package igym.entities;

import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Exercise createValidExercise() {
        Exercise ex = new Exercise();
        ex.setName("Squat");
        ex.setWeight(80);
        ex.setNumReps(10);
        ex.setNumSets(4);
        return ex;
    }

    @Test
    @DisplayName("Test valid workout")
    void testValidWorkout() {
        Workout workout = new Workout();
        workout.setWorkoutName("Upper Body");
        workout.setExerciseList(List.of(createValidExercise()));

        Set<ConstraintViolation<Workout>> violations = validator.validate(workout);
        assertTrue(violations.isEmpty(), "Expected no violations for valid workout");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" }) // 51 chars
    @DisplayName("Test invalid workout names")
    void testInvalidWorkoutNames(String invalidName) {
        Workout workout = new Workout();
        workout.setWorkoutName(invalidName);
        workout.setExerciseList(List.of(createValidExercise()));

        Set<ConstraintViolation<Workout>> violations = validator.validate(workout);
        assertFalse(violations.isEmpty(), "Expected violation for workout name: '" + invalidName + "'");
    }

    @Test
    @DisplayName("Test empty exercise list")
    void testEmptyExerciseList() {
        Workout workout = new Workout();
        workout.setWorkoutName("Leg Day");
        workout.setExerciseList(List.of());

        Set<ConstraintViolation<Workout>> violations = validator.validate(workout);
        assertFalse(violations.isEmpty(), "Expected violation for empty exercise list");
    }

    @Test
    @DisplayName("Test null exercise list")
    void testNullExerciseList() {
        Workout workout = new Workout();
        workout.setWorkoutName("Push Day");
        workout.setExerciseList(null);

        Set<ConstraintViolation<Workout>> violations = validator.validate(workout);
        assertFalse(violations.isEmpty(), "Expected violation for null exercise list");
    }

}
