package igym.controllers;

import igym.entities.Gym;
import igym.entities.enums.Status;
import igym.exceptions.DuplicateGymException;
import igym.exceptions.GymNotFoundException;
import igym.services.GymService;
import jakarta.validation.Validator;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        @Autowired
        private Validator validator;

        private final ObjectMapper objectMapper = new ObjectMapper();
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("Location 1");
        Gym gym1 = new Gym("Location 2");

        @BeforeEach
        void setUp() {
                assert validator != null;
                ReflectionTestUtils.setField(gym, "id", gymId);
        }

        @Test
        @DisplayName("should return a gym and status 201")
        void testCreateGymSuccess() throws Exception {

                when(gymService.createGym(any(Gym.class))).thenReturn(gym);

                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value(gym.getName()));

                verify(gymService, times(1)).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return 409 Conflict when gym name already exists")
        void testCreateGymWithDuplicateName() throws Exception {


                when(gymService.createGym(any(Gym.class)))
                                .thenThrow(new DuplicateGymException("A gym with the name 'Gold Gym' already exists."));

                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("A gym with the name 'Gold Gym' already exists."))
                                .andExpect(jsonPath("$.status").value(409))
                                .andExpect(jsonPath("$.error").value("Conflict"));

                verify(gymService, times(1)).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return 400 Bad Request when name is null")
        void testCreateGymWithNullName() throws Exception {
                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym(null))))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(gymService, never()).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return 400 Bad Request when name an empty string")
        void testCreateGymWithEmptyStringName() throws Exception {
                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym(""))))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors", hasSize(2))) // Ensure two errors exist
                                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                                                "Name cannot be blank",
                                                "Name must be between 3 and 50 characters")));

                verify(gymService, never()).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return 400 Bad Request when name has more than 50 characters")
        void testCreateGymWithTooLongName() throws Exception {
                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym("a".repeat(51)))))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(gymService, never()).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return 400 Bad Request when name has less than 3 characters")
        void testCreateGymWithTooShortName() throws Exception {
                mockMvc.perform(post("/api/gyms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym("a"))))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(gymService, never()).createGym(any(Gym.class));
        }

        @Test
        @DisplayName("should return all the gyms from the service and status 200")
        void testFindAllGymsSuccess() throws Exception {

                List<Gym> gyms = Arrays.asList(gym, gym1);

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

        @Test
        @DisplayName("should delete a gym from the repository")
        void testDeleteGymSuccess() throws Exception {
                assertEquals(Status.active, gym.getStatus(), "Gym should initially be active");
                doAnswer(invocation -> {
                        gym.setStatus(Status.inactive);
                        return null;
                }).when(gymService).deleteGym(gymId);

                mockMvc.perform(delete("/api/gyms/{id}", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                assertEquals(Status.inactive, gym.getStatus(), "Gym should finally be inactive");
                verify(gymService, times(1)).deleteGym(gymId);
                verifyNoMoreInteractions(gymService);
        }

        @Test
        @DisplayName("should return 404 when deleting a non existing gym")
        void testDeleteGymNotFound() throws Exception {
                doThrow(new GymNotFoundException("Gym with id " + gymId + " not found."))
                                .when(gymService).deleteGym(gymId);
                mockMvc.perform(delete("/api/gyms/{id}", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(gymService, times(1)).deleteGym(gymId);
                verifyNoMoreInteractions(gymService);
        }

        @Test
        @DisplayName("should return status 404 when deleting a inactive gym")
        void testDeleteGymAlreadyDeleted() throws Exception {
                gym.setStatus(Status.inactive);
                doThrow(new GymNotFoundException("Gym with id " + gymId + " not found."))
                                .when(gymService).deleteGym(gymId);
                mockMvc.perform(delete("/api/gyms/{id}", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
                assertEquals(Status.inactive, gym.getStatus(), "Gym should finally be inactive");
                verify(gymService, times(1)).deleteGym(gymId);
                verifyNoMoreInteractions(gymService);
        }

}