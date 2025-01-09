package igym.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

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
    public void findAllTest() {
        UserEntity u1 = new UserEntity("John Snow");
        UserEntity u2 = new UserEntity("Maria Carl");

        List<UserEntity> listUsers = repository.saveAll(Arrays.asList(u1, u2));

        assertTrue(service.findAll().containsAll(listUsers));
    }

}
