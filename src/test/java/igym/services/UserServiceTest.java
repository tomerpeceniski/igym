package igym.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import igym.entities.UserEntity;
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
        UserEntity u1 = new UserEntity("John Snow");
        UserEntity u2 = new UserEntity("Maria Carl");

        repository.deleteAll();
        List<UserEntity> listUsers = repository.saveAll(Arrays.asList(u1, u2));

        assertEquals(listUsers, service.findAll());
    }

    @Test
    @DisplayName("Test getting empty list of users")
    public void emptyListTest() {
        repository.deleteAll();
        List<UserEntity> list = service.findAll();
        assertTrue(list.isEmpty());
    }

}
