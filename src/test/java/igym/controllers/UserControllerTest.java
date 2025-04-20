package igym.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import igym.entities.User;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.services.UserService;
import jakarta.validation.Validator;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserService userService;

        @Autowired
        Validator validator;

        private List<User> users;
        private final ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        public void configurations() {
                users = new ArrayList<>();
                users.add(new User("Maria Clown"));
                users.add(new User("John Textor"));
        }

        @Test
        @DisplayName("Should return all saved users")
        public void findAllTest() throws Exception {
                when(userService.findAll()).thenReturn(users);

                mockMvc.perform(get("/users"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                                .andExpect(jsonPath("$[1].name").value(users.get(1).getName()));
        }

        @Test
        @DisplayName("Should return an empty list of users")
        public void findEmptyTest() throws Exception {
                when(userService.findAll()).thenReturn(new ArrayList<User>());

                mockMvc.perform(get("/users"))
                                .andExpect(status().isOk())
                                .andExpect(content().json("[]"));
        }

        @Test
        @DisplayName("Should return an error and status 500")
        public void errorGettingUsersTest() throws Exception {
                when(userService.findAll()).thenThrow(new RuntimeException("Internal server error"));

                mockMvc.perform(get("/users"))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Internal server error"));
        }

        @Test
        @DisplayName("Should return status 204 when deleting user")
        void deleteUserTest() throws Exception {
                UUID randomUuid = UUID.randomUUID();
                doNothing().when(userService).deleteUser(randomUuid);

                mockMvc.perform(delete("/users/" + randomUuid))
                                .andExpect(status().isNoContent());

                verify(userService, times(1)).deleteUser(randomUuid);
        }

        @Test
        @DisplayName("Should return status 404 when deleting user that does not exist")
        void deleteNonExistentUserTest() throws Exception {
                UUID randomUuid = UUID.randomUUID();
                doThrow(new UserNotFoundException("User with id " + randomUuid + " not found."))
                                .when(userService).deleteUser(randomUuid);

                mockMvc.perform(delete("/users/" + randomUuid))
                                .andExpect(status().isNotFound());

                verify(userService, times(1)).deleteUser(randomUuid);
        }

        @DisplayName("Should return the created user when creation was successful")
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
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with null name")
        void createNullNameUserTest() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new User())))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with blank name")
        void createBlankNameUserTest() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new User("     "))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with empty name")
        void createEmptyNameUserTest() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new User(""))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.errors", hasSize(2))) // Ensure two errors exist
                                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                                                "Name cannot be blank",
                                                "Name must be between 3 and 50 characters")));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with name with more than 50 characters")
        void createBigNameUserTest() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new User("a".repeat(51)))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with name with less than 3 characters")
        void createSmallNameUserTest() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new User("a"))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 409 when creating duplicate user")
        void createDuplicateUserTest() throws Exception {
                when(userService.createUser(any(User.class))).thenThrow(
                                new DuplicateUserException("An user with the name " + users.get(0).getName()
                                                + " already exists."));

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(users.get(0))))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("An user with the name " + users.get(0).getName()
                                                                + " already exists."))
                                .andExpect(jsonPath("$.error").value("Conflict"));

                verify(userService, times(1)).createUser(any(User.class));
        }
}
