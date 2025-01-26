package igym.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import igym.entities.User;
import igym.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    UserController controller;

    @Autowired
    UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test finding all Users")
    public void findAllTest() {
        repository.deleteAll();
        User user1 = new User("Maria Clown");
        User user2 = new User("Johan Marry");
        List<User> savedList = repository.saveAll(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = controller.findAll();
        assertEquals(savedList, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test finding empty list of Users")
    public void findEmptyTest() {
        repository.deleteAll();
        ResponseEntity<List<User>> response = controller.findAll();
        List<User> list = response.getBody();
        assertTrue(list.isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Testing status of response")
    public void statusTest() {
        repository.deleteAll();
        User user1 = new User("Maria Clown");
        User user2 = new User("Johan Marry");
        repository.saveAll(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response1 = controller.findAll();
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        repository.deleteAll();
        ResponseEntity<List<User>> response2 = controller.findAll();
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    @DisplayName("Testing method findById")
    public void findByIdTest() throws Exception {
        User user = new User("Maria Marry");
        user = repository.save(user);
        ResponseEntity<User> response1 = controller.findById(user.getId());
        assertEquals(user, response1.getBody());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        mockMvc.perform(get("/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not found"));
    }
}
