package igym.controllers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import igym.entities.User;
import igym.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
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
}
