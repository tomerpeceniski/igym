package igym.entities;

import jakarta.validation.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import igym.entities.enums.Status;

class ExerciseTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Test valid exercise")
    void testValidExercise() {
        Exercise ex = new Exercise();
        ex.setName("Bench Press");
        ex.setWeight(60);
        ex.setNumReps(10);
        ex.setNumSets(4);
        ex.setNote("Upper body");

        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertTrue(violations.isEmpty(), "Expected no violations for a valid exercise");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "AB", "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" }) // 51 chars
    @DisplayName("Test invalid exercise names")
    void testInvalidExerciseNames(String invalidName) {
        Exercise ex = new Exercise();
        ex.setName(invalidName);
        ex.setWeight(40);
        ex.setNumReps(10);
        ex.setNumSets(3);

        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertFalse(violations.isEmpty(), "Expected violation for name: " + invalidName);
    }

    @ParameterizedTest
    @ValueSource(doubles = { -10, -0.1 })
    @DisplayName("Test invalid exercise weights")
    void testInvalidWeights(double invalidWeight) {
        Exercise ex = new Exercise();
        ex.setName("Deadlift");
        ex.setWeight(invalidWeight);
        ex.setNumReps(10);
        ex.setNumSets(3);

        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertFalse(violations.isEmpty(), "Expected violation for weight: " + invalidWeight);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    @DisplayName("Test invalid number of repetitions")
    void testInvalidNumReps(int invalidReps) {
        Exercise ex = new Exercise();
        ex.setName("Push-up");
        ex.setWeight(0);
        ex.setNumReps(invalidReps);
        ex.setNumSets(3);

        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertFalse(violations.isEmpty(), "Expected violation for reps: " + invalidReps);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -2 })
    @DisplayName("Test invalid number of sets")
    void testInvalidNumSets(int invalidSets) {
        Exercise ex = new Exercise();
        ex.setName("Pull-up");
        ex.setWeight(0);
        ex.setNumReps(10);
        ex.setNumSets(invalidSets);

        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertFalse(violations.isEmpty(), "Expected violation for sets: " + invalidSets);
    }

    @Test
    @DisplayName("Test exercise default status is active")
    void testExerciseDefaultStatus() {
        Exercise ex = new Exercise();
        ex.setName("Lunge");
        ex.setWeight(40);
        ex.setNumReps(12);
        ex.setNumSets(3);
        Set<ConstraintViolation<Exercise>> violations = validator.validate(ex);
        assertTrue(violations.isEmpty());
        assertEquals(Status.active, ex.getStatus(), "Default status should be active");
    }

}
