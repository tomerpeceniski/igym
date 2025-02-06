package igym.controllers;

import igym.entities.Gym;
import igym.exceptions.DuplicateGymException;
import igym.services.GymService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(GymController.class)
@ExtendWith(MockitoExtension.class)
public class GymControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private GymService gymService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("should return a gym and status 201")
    void testCreateGymSuccess() throws Exception {

        Gym gym = new Gym("CrossFit Gym");
        when(gymService.createGym(any(Gym.class))).thenReturn(gym);

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("CrossFit Gym"));

        verify(gymService, times(1)).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return 409 Conflict when gym name already exists")
    void testCreateGymWithDuplicateName() throws Exception {

        Gym gym = new Gym("Gold Gym");

        when(gymService.createGym(any(Gym.class)))
                .thenThrow(new DuplicateGymException("A gym with the name 'Gold Gym' already exists."));

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("A gym with the name 'Gold Gym' already exists."))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));

        verify(gymService, times(1)).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return 400 Bad Request when name is null")
    void testCreateGymWithNullName() throws Exception {

        Gym gym = new Gym(null);

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        verify(gymService, never()).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return 400 Bad Request when name an empty string")
    void testCreateGymWithEmptyStringName() throws Exception {

        Gym gym = new Gym("");

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be blank"));

        verify(gymService, never()).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return 400 Bad Request when name has more than 50 characters")
    void testCreateGymWithTooLongName() throws Exception {

        Gym gym = new Gym("a".repeat(51));

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name must be between 3 and 50 characters"));

        verify(gymService, never()).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return 400 Bad Request when name has less than 3 characters")
    void testCreateGymWithTooShortName() throws Exception {

        Gym gym = new Gym("a");

        mockMvc.perform(post("/api/gyms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors.name").value("Name must be between 3 and 50 characters"));

        verify(gymService, never()).createGym(any(Gym.class));
    }

    @Test
    @DisplayName("should return all the gyms from the service and status 200")
    void testFindAllGymsSuccess() throws Exception {

        Gym gym1 = new Gym("Location 1");
        Gym gym2 = new Gym("Location 2");
        List<Gym> gyms = Arrays.asList(gym1, gym2);

        when(gymService.findAllGyms()).thenReturn(gyms);

        mockMvc.perform(get("/api/gyms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Location 1"))
                .andExpect(jsonPath("$[1].name").value("Location 2"));
    }

    @Test
    @DisplayName("should return an exception and status 500")
    void testFindAllGymsError() throws Exception {

        when(gymService.findAllGyms()).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get("/api/gyms"))
                .andExpect(status().isInternalServerError());
    }
}