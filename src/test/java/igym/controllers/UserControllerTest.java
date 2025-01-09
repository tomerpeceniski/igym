package igym.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import igym.entities.UserEntity;
import igym.repositories.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    UserController resource;

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Test finding all Users")
    public void findAllTest() {
        repository.deleteAll();        
        UserEntity user1 = new UserEntity("Maria Clown");
        UserEntity user2 = new UserEntity("Johan Marry");
        List<UserEntity> savedList = repository.saveAll(Arrays.asList(user1, user2));

        ResponseEntity<List<UserEntity>> response = resource.findAll();
        assertEquals(savedList, response.getBody());
    }

    @Test
    @DisplayName("Test finding empty list of Users")
    public void findEmptyTest() {
        repository.deleteAll();        
        ResponseEntity<List<UserEntity>> response = resource.findAll();
        List<UserEntity> list = response.getBody();
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Testing status of response")
    public void statusTest() {
        repository.deleteAll();        
        UserEntity user1 = new UserEntity("Maria Clown");
        UserEntity user2 = new UserEntity("Johan Marry");
        repository.saveAll(Arrays.asList(user1, user2));

        ResponseEntity<List<UserEntity>> response1 = resource.findAll();
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        repository.deleteAll();
        ResponseEntity<List<UserEntity>> response2 = resource.findAll();
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
}
