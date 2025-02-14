package igym.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserTest {

    Validator validator;

    @BeforeEach
    public void config() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        }


    @Test
    @DisplayName("Getting user name")
    public void userNameTest() {
        String userName = "Maria Clown";
        User user = new User();
        user.setName(userName);
        assertEquals(user.getName(), userName);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Testing ID from User")
    public void userIdTest() {
        String name1 = "Maria Clown";
        User user1 = new User(name1);
        assertNull(user1.getId(), "When a User is created, the ID should be null");
    }

    @Test
    @DisplayName("Testing null constraint")
    public void nullConstraintTest() {
        User nullNameUser = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(nullNameUser);
        assertTrue(violations.size() == 1);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Testing blank constraint")
    public void blankConstraintTest() {
        User blankNameUser = new User("");
        Set<ConstraintViolation<User>> violations = validator.validate(blankNameUser);
        assertTrue(violations.size() == 1);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());

        blankNameUser = new User(" ");
        violations = validator.validate(blankNameUser);
        assertTrue(violations.size() == 1);
        violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Testing maximum and minimum characters constraint")
    public void maximumCharacterConstraintTest() {
        User bigCharacterUser = new User("a".repeat(51));
        Set<ConstraintViolation<User>> violationsBigCharacter = validator.validate(bigCharacterUser);
        assertTrue(violationsBigCharacter.size() == 1);
        ConstraintViolation<User> violationBig = violationsBigCharacter.iterator().next();
        assertEquals(Size.class, violationBig.getConstraintDescriptor().getAnnotation().annotationType());

        User smallCharacterUser = new User("a");
        Set<ConstraintViolation<User>> violationsSmallCharacter = validator.validate(smallCharacterUser);
        assertTrue(violationsSmallCharacter.size() == 1);
        ConstraintViolation<User> violationSmall = violationsSmallCharacter.iterator().next();
        assertEquals(Size.class, violationSmall.getConstraintDescriptor().getAnnotation().annotationType());
    }
}