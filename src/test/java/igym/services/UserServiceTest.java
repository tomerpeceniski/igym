package igym.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import igym.entities.User;
import igym.repositories.UserRepository;
import igym.services.exceptions.DuplicateUserException;

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
    @DisplayName("Test getting users")
    public void findAllTest() {
        when(repository.findAll()).thenReturn(users);
        List<User> listUsers = service.findAll();
        assertIterableEquals(listUsers, users);
    }

    @Test
    @DisplayName("Test getting empty list of users")
    public void emptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        List<User> list = service.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Test create user")
    public void createUserTest() {
        when(repository.save(users.get(0))).thenReturn(users.get(0));
        User savedUser = service.createUser(users.get(0));
        assertEquals(users.get(0), savedUser);
        verify(repository, times(1)).save(users.get(0));
    }

    @Test
    @DisplayName("Test create duplicate user")
    public void createDuplicateUserTest() {
        when(repository.existsByName(users.get(0).getName())).thenReturn(true);
        assertThrows(DuplicateUserException.class, () -> service.createUser(users.get(0)));
        verify(repository, never()).save(users.get(0));
    }
}