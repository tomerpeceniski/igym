package igym.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import igym.entities.User;
import igym.repositories.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserRepository repository;

    @Autowired
    UserService service;

    @Test
    @DisplayName("Test getting users")
    public void findAllTest() {
        User u1 = new User("John Snow");
        User u2 = new User("Maria Carl");

        repository.deleteAll();
        List<User> listUsers = repository.saveAll(Arrays.asList(u1, u2));

        assertEquals(listUsers, service.findAll());
    }

    @Test
    @DisplayName("Test getting empty list of users")
    public void emptyListTest() {
        repository.deleteAll();
        List<User> list = service.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Test of getById method")
    public void findByIdTest() {
        User user = new User("John Snow");
        user = repository.save(user);
        User foundUser = service.findById(user.getId());
        assertTrue(user.equals(foundUser)); 
    }

    @Test
    @DisplayName("Test of getById method")
    public void findByIdErrorTest() {
        assertThrowsExactly(ObjectNotFoundException.class, () -> service.findById(UUID.randomUUID()));
    }

}
