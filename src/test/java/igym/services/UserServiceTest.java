package igym.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.User;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.repositories.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GymService gymService;
    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user1;
    private User user2;

    @BeforeEach
    void config() {
        userId = UUID.randomUUID();
        user1 = new User("Maria Clown");
        user2 = new User("John Textor");
    }

    @Test
    @DisplayName("Should return the list of saved users")
    void findAllTest() {
        List<User> users = List.of(user1, user2);
        
        when(userRepository.findByStatus(Status.active)).thenReturn(users);

        List<User> listUsers = userService.findAll();
        assertThat(listUsers).hasSize(2);
        assertThat(listUsers).containsExactlyInAnyOrderElementsOf(users);
    }

    @Test
    @DisplayName("Should return an empty list if no user was saved")
    void emptyListTest() {
        when(userRepository.findByStatus(Status.active)).thenReturn(List.of());
        List<User> list = userService.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Should inactivate user and all its gyms")
    void deleteUserTest() {
        Gym gym = new Gym("Mocked Gym");
        Gym gymInactive = new Gym("Mocked Inactive Gym");
        gymInactive.setStatus(Status.inactive);
        Exercise exercise = new Exercise();
        exercise.setName("Mocked Exercise");
        Workout workout = new Workout();
        workout.setName("Mocked Workout");
        workout.setExerciseList(List.of(exercise));
        gym.setWorkouts(List.of(workout));
        user1.setGyms(List.of(gym, gymInactive));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        
        doAnswer(invocation -> {
            gym.setStatus(Status.inactive);
            gym.getWorkouts().forEach(w -> {
                w.setStatus(Status.inactive);
                w.getExerciseList().forEach(e -> e.setStatus(Status.inactive));
            });
        return null;
    }).when(gymService).deleteGym(gym.getId());
        
        userService.deleteUser(user1.getId());
        assertEquals(Status.inactive, user1.getStatus());
        assertEquals(Status.inactive, gym.getStatus());
        assertEquals(Status.inactive, gymInactive.getStatus());
        assertEquals(Status.inactive, workout.getStatus());
        assertEquals(Status.inactive, exercise.getStatus());
    }

    @Test
    @DisplayName("Should inactivate user when list of gyms is empty")
    void deleteUserEmptyGyms() {
        user1.setGyms(List.of());
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        
        userService.deleteUser(user1.getId());
        assertEquals(Status.inactive, user1.getStatus());
    }

    @Test
    @DisplayName("Should inactivate user when list of gyms is null")
    void deleteUserNullGyms() {
        user1.setGyms(null);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        
        userService.deleteUser(user1.getId());
        assertEquals(Status.inactive, user1.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when trying to delete inexistent user")
    void deleteNonExistentUserTest() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());
        assertThrowsExactly(UserNotFoundException.class, () -> userService.deleteUser(user1.getId()));
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete already deleted user")
    void deleteDeletedUserTest() {
        user1.setStatus(Status.inactive);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        assertThrowsExactly(UserNotFoundException.class, () -> userService.deleteUser(user1.getId()));
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("Should return a list that contains the saved user")
    void createUserTest() {
        when(userRepository.save(user1)).thenReturn(user1);
        User savedUser = userService.createUser(user1);
        assertEquals(user1, savedUser);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    @DisplayName("Should return an error when trying to create a duplicate user with status active")
    void createDuplicateUserTest() {
        when(userRepository.existsByNameAndStatus(user1.getName(), Status.active)).thenReturn(true);
        assertThrows(DuplicateUserException.class, () -> userService.createUser(user1));
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("should successfully update a user when provided with a valid new name")
    void testUpdateUser() {
        String name = user2.getName();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userRepository.existsByNameAndStatus(name, Status.active)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);
        User result = userService.updateUser(userId, name);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when attempting to update a user that does not exist")
    void testUpdateUserNotFound() {
        String name = user2.getName();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(userId, name));
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when attempting to update a user that is inactive")
    void testUpdateUserInactive() {
        String name = user2.getName();
        user1.setStatus(Status.inactive);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(userId, name));
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw DuplicateUserException when attempting to update a user to a name that is already in use and if the updated name is the same as the existing one user active")
    void testUpdateUserExistingName() {
        String name = user2.getName();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userRepository.existsByNameAndStatus(name, Status.active)).thenReturn(true);
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> userService.updateUser(userId, name));
        assertEquals("A user with the name '" + name + "' already exists.",
                exception.getMessage());
        verify(userRepository, times(1)).existsByNameAndStatus(name, Status.active);
        verify(userRepository, never()).save(user1);
    }

    @Test
    @DisplayName("Should return the user when an existent user is passed")
    void testFindExistentById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        User returnedUser = userService.findById(userId);

        assertEquals(returnedUser, user1);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should return UserNotFoundException when a non existent user is passed")
    void testFindNonExistentById() {
        when(userRepository.findById(userId)).thenThrow(new UserNotFoundException("User with id " + userId + " not found"));
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should return UserNotFoundException when an inactive user is passed")
    void testFindInactiveById() {
        user1.setStatus(Status.inactive);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}
