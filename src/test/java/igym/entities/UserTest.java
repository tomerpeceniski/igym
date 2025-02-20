package igym.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import igym.config.ValidationConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@SpringBootTest
public class UserTest {

    Validator validator = ValidationConfig.validator();

    @Test
    @DisplayName("Should return the name of the user")
    public void userNameTest() {
        String userName = "Maria Clown";
        User user = new User();
        user.setName(userName);
        assertEquals(user.getName(), userName);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return ID null when an User is created")
    public void userIdTest() {
        String name1 = "Maria Clown";
        User user1 = new User(name1);
        assertNull(user1.getId(), "When a User is created, the ID should be null");
    }

    @Test
    @DisplayName("Should return violation error due to null name")
    public void nullConstraintTest() {
        User nullNameUser = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(nullNameUser);
        assertTrue(violations.size() == 1);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Should return violation error due to empty name")
    public void emptyConstraintTest() {
        User emptyNameUser = new User("");
        Set<ConstraintViolation<User>> violations = validator.validate(emptyNameUser);
        assertThat(violations).hasSize(2);

        Set<Class<?>> expectedViolations = Set.of(NotBlank.class, Size.class);
        Set<Class<?>> actualViolations = violations.stream()
                .map(v -> v.getConstraintDescriptor().getAnnotation().annotationType())
                .collect(Collectors.toSet());

        assertThat(actualViolations).containsExactlyInAnyOrderElementsOf(expectedViolations);
    }

    @Test
    @DisplayName("Should return violation error due to blank name")
    public void blankConstraintTest() {
        User blankNameUser = new User("      ");
        Set<ConstraintViolation<User>> violations = validator.validate(blankNameUser);
        assertTrue(violations.size() == 1);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Should return violation error due to name bigger of maximum number os characters")
    public void maximumCharacterConstraintTest() {
        User bigCharacterUser = new User("a".repeat(51));
        Set<ConstraintViolation<User>> violationsBigCharacter = validator.validate(bigCharacterUser);
        assertTrue(violationsBigCharacter.size() == 1);
        ConstraintViolation<User> violationBig = violationsBigCharacter.iterator().next();
        assertEquals(Size.class, violationBig.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Should return violation error due to name smaller of minimum number os characters")
    public void minimumCharacterConstraintError() {
        User smallCharacterUser = new User("a");
        Set<ConstraintViolation<User>> violationsSmallCharacter = validator.validate(smallCharacterUser);
        assertTrue(violationsSmallCharacter.size() == 1);
        ConstraintViolation<User> violationSmall = violationsSmallCharacter.iterator().next();
        assertEquals(Size.class, violationSmall.getConstraintDescriptor().getAnnotation().annotationType());
    }
}