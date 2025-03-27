package igym.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.repositories.UserRepository;

public class UserServiceTest {

    private UserRepository repository = mock(UserRepository.class);
    private UserService service = new UserService(repository);
    private List<User> users;

    @BeforeEach
    public void configurations() {
        users = new ArrayList<>();
        users.add(new User("Maria Clown"));
        users.add(new User("John Textor"));
    }

    @Test
    @DisplayName("Should return the list of saved users")
    public void findAllTest() {
        when(repository.findAll()).thenReturn(users);
        List<User> listUsers = service.findAll();
        assertIterableEquals(listUsers, users);
    }

    @Test
    @DisplayName("Should return an empty list if no user was saved")
    public void emptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        List<User> list = service.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Should delete user from repository")
    public void deleteUserTest() {
        User user = users.get(0);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        service.deleteUser(user.getId());
        assertEquals(user.getStatus(), Status.inactive);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete inexistent user")
    public void deleteNonExistentUserTest() {
        User user = users.get(0);
        when(repository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrowsExactly(UserNotFoundException.class, () -> service.deleteUser(user.getId()));
        verify(repository, never()).save(user);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete already deleted user")
    public void deleteDeletedUserTest() {
        User user = users.get(0);
        user.setStatus(Status.inactive);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThrowsExactly(UserNotFoundException.class, () -> service.deleteUser(user.getId()));
        verify(repository, never()).save(user);
    }

    @DisplayName("Should return a list that contains the saved user")
    public void createUserTest() {
        when(repository.save(users.get(0))).thenReturn(users.get(0));
        User savedUser = service.createUser(users.get(0));
        assertEquals(users.get(0), savedUser);
        verify(repository, times(1)).save(users.get(0));
    }

    @Test
    @DisplayName("Should return an error when trying to create a duplicate user")
    public void createDuplicateUserTest() {
        when(repository.existsByName(users.get(0).getName())).thenReturn(true);
        assertThrows(DuplicateUserException.class, () -> service.createUser(users.get(0)));
        verify(repository, never()).save(users.get(0));
    }
}
