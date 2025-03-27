package igym.entities;

import jakarta.transaction.Transactional;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Rollback;

import igym.entities.enums.Status;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
class GymTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("should create a gym successfully when name is valid")
    void testValidGym() {
        Gym gym = new Gym("Valid Name");
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertTrue(violations.isEmpty());
        assertTrue(gym.getStatus() == Status.active);
    }

    @Test
    @DisplayName("should return a validation error when the name is blank")
    void testBlankName() {
        Gym gym = new Gym("");
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertFalse(violations.isEmpty());

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.contains("Name cannot be blank"));
    }

    @Test
    @DisplayName("should return a validation error when the name is null")
    void testNullName() {
        Gym gym = new Gym(null);
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertFalse(violations.isEmpty());

        List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertTrue(messages.contains("Name cannot be blank"));
    }

    @Test
    @DisplayName("should return a validation error when the name is too short (less than 3 characters)")
    void testNameTooShort() {
        Gym gym = new Gym("AB");
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertFalse(violations.isEmpty());
        assertEquals("Name must be between 3 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should return a validation error when the name is too long (more than 50 characters)")
    void testNameTooLong() {
        Gym gym = new Gym("A".repeat(51));
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertFalse(violations.isEmpty());
        assertEquals("Name must be between 3 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should set the gym status")
    void testSetGymStatus(){
        Gym gym = new Gym();
        gym.setStatus(Status.inactive);
        Set<ConstraintViolation<Gym>> violations = validator.validate(gym);
        assertFalse(violations.isEmpty());
        assertTrue(gym.getStatus() == Status.inactive);
    }


}