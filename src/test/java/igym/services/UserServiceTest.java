package igym.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import igym.entities.User;
import igym.exceptions.ObjectNotFoundException;
import igym.repositories.UserRepository;

public class UserServiceTest {

    private UserRepository repository = mock(UserRepository.class);
    private UserService service = new UserService(repository);
    static List<User> users;

    @BeforeAll
    public static void configurations() {
        users = new ArrayList<>();
        users.add(new User("Maria Clown"));
        users.add(new User("John Textor"));
    }

    @Test
    @DisplayName("Test getting users")
    public void findAllTest() {
        when(repository.findAll()).thenReturn(users);

        List<User> listUsers = service.findAll();

        assertEquals(listUsers, users);
    }

    @Test
    @DisplayName("Test getting empty list of users")
    public void emptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        List<User> list = service.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Should delete user from repository")
    public void deleteUserTest() {
        when(repository.existsById(users.get(0).getId())).thenReturn(true);
        service.deleteUser(users.get(0).getId());
        verify(repository, times(1)).deleteById(users.get(0).getId());
    }

    @Test
    @DisplayName("Should delete user from repository")
    public void deleteNonExistentUserTest() {
        when(repository.existsById(users.get(0).getId())).thenReturn(false);
        assertThrowsExactly(ObjectNotFoundException.class, () -> service.deleteUser(users.get(0).getId()));
        verify(repository, never()).deleteById(users.get(0).getId());
    }
}
