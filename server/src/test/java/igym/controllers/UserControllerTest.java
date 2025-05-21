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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import igym.dtos.LoginRequestDTO;
import igym.dtos.LoginResponseDTO;
import igym.entities.User;
import igym.exceptions.UserNotFoundException;
import igym.security.JwtAuthenticationFilter;
import igym.exceptions.DuplicateUserException;
import igym.exceptions.InvalidCredentialsException;
import igym.services.UserService;
import jakarta.validation.Validator;
import igym.dtos.UpdateUserNameDTO;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        Validator validator;

        private final ObjectMapper objectMapper = new ObjectMapper();
        private final UUID userId = UUID.randomUUID();
        private final User user1 = new User("Maria Clown");
        private final User user2 = new User("John Textor");


        @BeforeEach
        public void config() {
                user1.setPassword("validPassword");
        }

        @Test
        @DisplayName("Should return all saved users")
        public void findAllTest() throws Exception {
                List<User> users = new ArrayList<>();
                users.add(user1);
                users.add(user2);

                when(userService.findAll()).thenReturn(users);

                mockMvc.perform(get("/api/v1/users"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                                .andExpect(jsonPath("$[1].name").value(user2.getName()));
        }

        @Test
        @DisplayName("Should return an empty list of users")
        public void findEmptyTest() throws Exception {
                when(userService.findAll()).thenReturn(new ArrayList<User>());

                mockMvc.perform(get("/api/v1/users"))
                                .andExpect(status().isOk())
                                .andExpect(content().json("[]"));
        }

        @Test
        @DisplayName("Should return an error and status 500")
        public void errorGettingUsersTest() throws Exception {
                when(userService.findAll()).thenThrow(new RuntimeException("Internal server error"));

                mockMvc.perform(get("/api/v1/users"))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Internal server error"));
        }

        @Test
        @DisplayName("Should return status 204 when deleting user")
        void deleteUserTest() throws Exception {
                UUID randomUuid = UUID.randomUUID();
                doNothing().when(userService).deleteUser(randomUuid);

                mockMvc.perform(delete("/api/v1/users/" + randomUuid))
                                .andExpect(status().isNoContent());

                verify(userService, times(1)).deleteUser(randomUuid);
        }

        @Test
        @DisplayName("Should return status 404 when deleting user that does not exist")
        void deleteNonExistentUserTest() throws Exception {
                UUID randomUuid = UUID.randomUUID();
                doThrow(new UserNotFoundException("User with id " + randomUuid + " not found."))
                                .when(userService).deleteUser(randomUuid);

                mockMvc.perform(delete("/api/v1/users/" + randomUuid))
                                .andExpect(status().isNotFound());

                verify(userService, times(1)).deleteUser(randomUuid);
        }

        @DisplayName("Should return the created user when creation was successful")
        void createUserTest() throws Exception {
                when(userService.createUser(any(User.class))).thenReturn(user1);

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value(user1.getName()));

                verify(userService, times(1)).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with null name")
        void createNullNameUserTest() throws Exception {
                User user = new User();
                user.setPassword("validpassword");

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with blank name")
        void createBlankNameUserTest() throws Exception {
                user1.setName("     ");

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with empty name")
        void createEmptyNameUserTest() throws Exception {
                user1.setName("");

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
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
                user1.setName("a".repeat(51));

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 422 Unprocessable Entity when creating user with name with less than 3 characters")
        void createSmallNameUserTest() throws Exception {
                user1.setName("a");

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(userService, never()).createUser(any(User.class));
        }

        @Test
        @DisplayName("Should return status 409 when creating duplicate user")
        void createDuplicateUserTest() throws Exception {
                when(userService.createUser(any(User.class))).thenThrow(
                                new DuplicateUserException("An user with the name " + user1.getName()
                                                + " already exists."));

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("An user with the name " + user1.getName()
                                                                + " already exists."))
                                .andExpect(jsonPath("$.error").value("Conflict"));

                verify(userService, times(1)).createUser(any(User.class));
        }

        @Test
        @DisplayName("should update a existing user and return status 200")
        void testUpdateUserSuccess() throws Exception {
                when(userService.updateUser(userId, user1.getName())).thenReturn(user1);
                mockMvc.perform(patch("/api/v1/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(user1.getId()))
                                .andExpect(jsonPath("$.name").value(user1.getName()));
                verify(userService, times(1)).updateUser(userId, user1.getName());
        }

        @Test
        @DisplayName("should return 404 when trying to update a non existing user")
        void testUpdateUserNotFound() throws Exception {
                doThrow(new UserNotFoundException("User with id " + userId + " not found."))
                                .when(userService).updateUser(userId, user1.getName());
                mockMvc.perform(patch("/api/v1/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("User with id " + userId + " not found."))
                                .andExpect(jsonPath("$.error").value("Not Found"));
                verify(userService, times(1)).updateUser(userId, user1.getName());
        }

        @Test
        @DisplayName("should return 409 when trying to update a user with name already in use")
        void testUpdateUserNameAlreadyInUse() throws Exception {
                doThrow(new DuplicateUserException("A user with the name '" + user1.getName() + "' already exists."))
                                .when(userService).updateUser(userId, user1.getName());
                mockMvc.perform(patch("/api/v1/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("A user with the name '" + user1.getName()
                                                                + "' already exists."))
                                .andExpect(jsonPath("$.error").value("Conflict"));
                verify(userService, times(1)).updateUser(userId, user1.getName());
        }

        @Test
        @DisplayName("should return 422 when trying to update user with invalid name")
        void testUpdateUserInvalidName() throws Exception {
                UpdateUserNameDTO dto = new UpdateUserNameDTO("");

                mockMvc.perform(patch("/api/v1/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                                                "Name cannot be blank",
                                                "Name must be between 3 and 50 characters")));

                verify(userService, never()).updateUser(any(), any());
        }

        @Test
        @DisplayName("should return 200 when trying to login with valid credentials")
        void testLoginValidCredentials() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("user", "password");
                LoginResponseDTO response = new LoginResponseDTO("tokenExample", request.name());

                when(userService.authenticate(request.name(), request.password())).thenReturn(response);

                mockMvc.perform(post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(request.name()))
                                .andExpect(jsonPath("$.token").value(response.token()));
                                
                verify(userService, times(1)).authenticate(request.name(), request.password());
        }

        @Test
        @DisplayName("should return 400 when trying to login with non found user")
        void testLoginInvalidName() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("user", "password");
                
                doThrow(new UserNotFoundException("User not found"))
                                .when(userService).authenticate(request.name(),request.password());

                mockMvc.perform(post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("User not found"))
                                .andExpect(jsonPath("$.error").value("Not Found"));
                                
                verify(userService, times(1)).authenticate(request.name(), request.password());
        }

        @Test
        @DisplayName("should return 401 when trying to login invalid credentials")
        void testLoginInvalidCredentials() throws Exception {
                LoginRequestDTO request = new LoginRequestDTO("user", "password");

                doThrow(new InvalidCredentialsException("Invalid credentials provided"))
                                .when(userService).authenticate(request.name(),request.password());

                mockMvc.perform(post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message").value("Invalid credentials provided"))
                                .andExpect(jsonPath("$.error").value("Unauthorized"));
                                
                verify(userService, times(1)).authenticate(request.name(), request.password());
        }
}
