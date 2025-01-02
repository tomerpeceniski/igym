package igym.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import igym.entities.User;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired 
    UserRepository repository;

    @Test
    public void saveAndFindTest() {
        User user = new User("John Snow");
        User savedUser = repository.save(user);
        assertNotNull(savedUser.getId());

        Optional<User> optional = repository.findById(savedUser.getId());
        User findedUser = optional.get();
        assertEquals(findedUser, savedUser);
    }
}
