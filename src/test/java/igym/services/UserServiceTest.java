package igym.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import igym.entities.User;
import igym.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository repository;

    @Autowired
    UserService service;

    @Test
    public void findAllTest() {
        User u1 = new User("John Snow");
        User u2 = new User("Maria Carl");

        List<User> listUsers = repository.saveAll(Arrays.asList(u1, u2));

        assertTrue(service.findAll().containsAll(listUsers));
    }

}
