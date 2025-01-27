package igym.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import igym.entities.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;
    User[] users;

    @BeforeEach
    public void configurations() {
        repository.deleteAll();
        users = new User [] { new User("Maria Clown"), new User("John Textor") };
    }

    @Test
    @DisplayName("Persising user and returning it")
    public void saveUserTest() {
        User savedUser = repository.save(users[0]);
        assertEquals(users[0].getName(), savedUser.getName());

        List<User> listUsers = repository.findAll();
        User findedUser = listUsers.get(0);
        assertEquals(findedUser, savedUser);
    }

    @Test
    @DisplayName("Getting empty list of users")
    public void emptyUsersTest() {
        List<User> list = repository.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Ensuring that persisting creates ID")
    public void idCreationTest() {
        assertNull(users[0].getId());

        repository.save(users[0]);
        assertNotNull(users[0].getId());
    }

    @Test
    @DisplayName("findAll method")
    public void findAllTest() {
        List<User> addList = Arrays.asList(users);
        repository.saveAll(addList);

        List<User> findList = repository.findAll();
        assertTrue(addList.equals(findList));
    }
}
