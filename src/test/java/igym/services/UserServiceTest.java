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

import igym.entities.Gym;
import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.repositories.UserRepository;

class UserServiceTest {

    private UserRepository repository = mock(UserRepository.class);
    private UserService service = new UserService(repository);

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
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(repository.findAll()).thenReturn(users);
        List<User> listUsers = service.findAll();
        assertIterableEquals(listUsers, users);
    }

    @Test
    @DisplayName("Should return an empty list if no user was saved")
    void emptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        List<User> list = service.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Should inactivate user and all its gyms")
    void deleteUserTest() {
        user1.setGyms(List.of(new Gym("Mocked Gym")));
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        service.deleteUser(user1.getId());
        assertEquals(Status.inactive, user1.getStatus());
        assertEquals(Status.inactive, user1.getGyms().get(0).getStatus());
    }

    @Test
    @DisplayName("Should throw exception when trying to delete inexistent user")
    void deleteNonExistentUserTest() {
        when(repository.findById(user1.getId())).thenReturn(Optional.empty());
        assertThrowsExactly(UserNotFoundException.class, () -> service.deleteUser(user1.getId()));
        verify(repository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete already deleted user")
    void deleteDeletedUserTest() {
        user1.setStatus(Status.inactive);
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        assertThrowsExactly(UserNotFoundException.class, () -> service.deleteUser(user1.getId()));
        verify(repository, never()).save(user1);
    }

    @DisplayName("Should return a list that contains the saved user")
    void createUserTest() {
        when(repository.save(user1)).thenReturn(user1);
        User savedUser = service.createUser(user1);
        assertEquals(user1, savedUser);
        verify(repository, times(1)).save(user1);
    }

    @Test
    @DisplayName("Should return an error when trying to create a duplicate user with status active")
    void createDuplicateUserTest() {
        when(repository.existsByNameAndStatus(user1.getName(), Status.active)).thenReturn(true);
        assertThrows(DuplicateUserException.class, () -> service.createUser(user1));
        verify(repository, never()).save(user1);
    }

    @Test
    @DisplayName("should successfully update a user when provided with a valid new name")
    void testUpdateUser() {
        String name = user2.getName();
        when(repository.findById(userId)).thenReturn(Optional.of(user1));
        when(repository.existsByNameAndStatus(name, Status.active)).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user1);
        User result = service.updateUser(userId, name);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when attempting to update a user that does not exist")
    void testUpdateUserNotFound() {
        String name = user2.getName();
        when(repository.findById(userId)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> service.updateUser(userId, name));
        assertEquals("User with id " + userId + " not found.", exception.getMessage());
        verify(repository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when attempting to update a user that is inactive")
    void testUpdateUserInactive() {
        String name = user2.getName();
        user1.setStatus(Status.inactive);
        when(repository.findById(userId)).thenReturn(Optional.of(user1));
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> service.updateUser(userId, name));
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(repository, never()).save(user1);
    }

    @Test
    @DisplayName("Should throw DuplicateUserException when attempting to update a user to a name that is already in use and if the updated name is the same as the existing one user active")
    void testUpdateUserExistingName() {
        String name = user2.getName();
        when(repository.findById(userId)).thenReturn(Optional.of(user1));
        when(repository.existsByNameAndStatus(name, Status.active)).thenReturn(true);
        DuplicateUserException exception = assertThrows(
                DuplicateUserException.class,
                () -> service.updateUser(userId, name));
        assertEquals("A user with the name '" + name + "' already exists.",
                exception.getMessage());
        verify(repository, times(1)).existsByNameAndStatus(name, Status.active);
        verify(repository, never()).save(user1);
    }

    @Test
    @DisplayName("Should return the user when an existent user is passed")
    void testFindExistentById() {
        when(repository.findById(userId)).thenReturn(Optional.of(user1));
        User returnedUser = service.findById(userId);

        assertEquals(returnedUser, user1);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should return UserNotFoundException when a non existent user is passed")
    void testFindNonExistentById() {
        when(repository.findById(userId)).thenThrow(new UserNotFoundException("User with id " + userId + " not found"));
        assertThrows(UserNotFoundException.class, () -> service.findById(userId));
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should return UserNotFoundException when an inactive user is passed")
    void testFindInactiveById() {
        user1.setStatus(Status.inactive);
        when(repository.findById(userId)).thenReturn(Optional.of(user1));
        assertThrows(UserNotFoundException.class, () -> service.findById(userId));
        verify(repository, times(1)).findById(userId);
    }
}
