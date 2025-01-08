package igym.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import igym.entities.UserEntity;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired 
    UserRepository repository;

    @Test
    public void saveAndFindTest() {
        UserEntity user = new UserEntity("John Snow");
        UserEntity savedUser = repository.save(user);
        assertNotNull(savedUser.getId());

        Optional<UserEntity> optional = repository.findById(savedUser.getId());
        UserEntity findedUser = optional.get();
        assertEquals(findedUser, savedUser);
    }
}
