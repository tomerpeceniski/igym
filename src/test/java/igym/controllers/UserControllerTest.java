package igym.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import igym.entities.User;
import igym.services.UserService;
import igym.services.exceptions.DuplicateUserException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private List<User> users;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void configurations() {
        users = new ArrayList<>();
        users.add(new User("Maria Clown"));
        users.add(new User("John Textor"));
    }

    @Test
    @DisplayName("Find all Users")
    public void findAllTest() throws Exception {
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).getName()));
    }

    @Test
    @DisplayName("Find empty list of Users")
    public void findEmptyTest() throws Exception {
        when(userService.findAll()).thenReturn(new ArrayList<User>());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Test for error while getting Users")
    public void errorGettingUsersTest() throws Exception {
        when(userService.findAll()).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }

    @Test
    @DisplayName("Create new User")
    void createUserTest() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(users.get(0));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users.get(0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(users.get(0).getName()));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Error creating new User with null name")
    void createNullNameUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    @DisplayName("Error creating new User with blank name")
    void createEmptyNameUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User(""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User("     "))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    @DisplayName("Error creating new User with name with more than 50 characters or less then 3")
    void createBigNameUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User("a".repeat(51)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name must be between 3 and 50 characters"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User("a"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name must be between 3 and 50 characters"));

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    @DisplayName("Error creating duplicate User")
    void createDuplicateUserTest() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new DuplicateUserException("An user with the name " + users.get(0).getName() + " already exists."));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(users.get(0))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("An user with the name " + users.get(0).getName() + " already exists."))
                .andExpect(jsonPath("$.error").value("Conflict"));

        verify(userService, times(1)).createUser(any(User.class));
    }
}
