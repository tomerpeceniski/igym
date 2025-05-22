package igym.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import igym.entities.enums.Status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should return the name of the user")
    public void userNameTest() {
        String userName = "Maria Clown";
        String password = "123456";
        User user = new User();
        user.setName(userName);
        user.setPassword(password);
        
        assertEquals(user.getName(), userName);
        assertEquals(user.getPassword(), password);
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
    @DisplayName("Should return status active when an User is created")
    public void userStatusTest() {
        User user = new User("Maria Clown");
        assertEquals(user.getStatus(), Status.active);
    }

    @Test
    @DisplayName("Should return status inactive after changing status to inactive")
    public void changeStatusTest() {
        User user = new User("Maria Clown");
        assertEquals(user.getStatus(), Status.active);
        user.setStatus(Status.inactive);
        assertEquals(user.getStatus(), Status.inactive);
    }

    @Test
    @DisplayName("Should return violation errors due to null name")
    public void nullConstraintTest() {
        User nullNameUser = new User();
        nullNameUser.setPassword("password");
        Set<ConstraintViolation<User>> violations = validator.validate(nullNameUser);
        assertThat(violations).hasSize(1);
    
        Set<Class<?>> expectedViolations = Set.of(NotBlank.class);
        Set<Class<?>> actualViolations = violations.stream()
                .map(v -> v.getConstraintDescriptor().getAnnotation().annotationType())
                .collect(Collectors.toSet());
    
        assertThat(actualViolations).containsExactlyInAnyOrderElementsOf(expectedViolations);
    }
    

    @Test
    @DisplayName("Should return violation errors due to empty name")
    public void emptyConstraintTest() {
        User emptyNameUser = new User();
        emptyNameUser.setName("");
        emptyNameUser.setPassword("validPassword123");
    
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
        blankNameUser.setPassword("validPassword123");
        Set<ConstraintViolation<User>> violations = validator.validate(blankNameUser);
        assertTrue(violations.size() == 1);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Should return violation error due to name bigger of maximum number os characters")
    public void maximumCharacterConstraintTest() {
        User bigCharacterUser = new User("a".repeat(51));
        bigCharacterUser.setPassword("validPassword123"); 
        Set<ConstraintViolation<User>> violationsBigCharacter = validator.validate(bigCharacterUser);
        assertTrue(violationsBigCharacter.size() == 1);
        ConstraintViolation<User> violationBig = violationsBigCharacter.iterator().next();
        assertEquals(Size.class, violationBig.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    @DisplayName("Should return violation error due to name smaller of minimum number os characters")
    public void minimumCharacterConstraintError() {
        User smallCharacterUser = new User("a");
        smallCharacterUser.setPassword("validPassword123");
        Set<ConstraintViolation<User>> violationsSmallCharacter = validator.validate(smallCharacterUser);
        assertTrue(violationsSmallCharacter.size() == 1);
        ConstraintViolation<User> violationSmall = violationsSmallCharacter.iterator().next();
        assertEquals(Size.class, violationSmall.getConstraintDescriptor().getAnnotation().annotationType());
    }
}