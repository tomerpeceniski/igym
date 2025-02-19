package igym.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import igym.entities.User;
import igym.exceptions.ObjectNotFoundException;
import igym.services.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    public static List<User> users;

    @BeforeAll
    public static void configurations() {
        users = new ArrayList<>();
        users.add(new User("Maria Clown"));
        users.add(new User("John Textor"));
    }

    @Test
    @DisplayName("Finding all Users")
    public void findAllTest() throws Exception {
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).getName()));
    }

    @Test
    @DisplayName("Finding empty list of Users")
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
        doThrow(new ObjectNotFoundException("There is no User with ID: " + randomUuid))
    .when(userService).deleteUser(randomUuid);

        mockMvc.perform(delete("/users/" + randomUuid))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(randomUuid);
    }
}
