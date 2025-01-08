package igym.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import igym.entities.UserEntity;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired 
    UserRepository repository;

    @Test
    @DisplayName("Testing persisting User in the DB and getting it again by ID")
    public void saveUserTest() {
        String name1 = "John Snow";
        UserEntity user1 = new UserEntity(name1);
        UserEntity savedUser = repository.save(user1);
        assertEquals(user1.getName(), savedUser.getName());

        Optional<UserEntity> optional = repository.findById(savedUser.getId());
        UserEntity findedUser = optional.get();
        assertEquals(findedUser, savedUser);
    }

    @Test
    @DisplayName("Testing getting empty list of users")
    public void emptyUsersTest() {
        repository.deleteAll();
        List<UserEntity> list = repository.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Ensuring that persisting creates ID")
    public void idCreationTest() {
        UserEntity user1 = new UserEntity("Maria clown");
        assertNull(user1.getId());

        repository.save(user1);
        assertNotNull(user1.getId());
    }

    @Test
    @DisplayName("Testing findAll method")
    public void findAllTest() {
        UserEntity user1 = new UserEntity("Maria Clown");
        UserEntity user2 = new UserEntity("Jhonny Brave");
        
        List<UserEntity> addList = Arrays.asList(user1, user2);
        repository.deleteAll();
        repository.saveAll(addList);

        List<UserEntity> findList = repository.findAll();
        assertTrue(addList.equals(findList));
    }
}
