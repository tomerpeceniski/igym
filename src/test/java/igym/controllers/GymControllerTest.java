package igym.controllers;

import igym.entities.Gym;
import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.DuplicateGymException;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.UserNotFoundException;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
        Validator validator;

        private final ObjectMapper objectMapper = new ObjectMapper();
        UUID gymId;
        UUID userId;
        Gym gym1;
        Gym gym2;

        @BeforeEach
        void setUp() {
                User user = new User("Mocked User");
                ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
                gym1 = new Gym("Location 1");
                gym2 = new Gym("Location 2");
                ReflectionTestUtils.setField(gym1, "id", UUID.randomUUID());
                gym1.setUser(user);
                ReflectionTestUtils.setField(gym1, "updated_at", Instant.now());
                ReflectionTestUtils.setField(gym2, "id", UUID.randomUUID());
                gym2.setUser(user);
                ReflectionTestUtils.setField(gym2, "updated_at", Instant.now());
                gymId = gym1.getId();
                userId = user.getId();
        }

        @Test
        @DisplayName("should return a gym and status 201")
        void testCreateGymSuccess() throws Exception {
                when(gymService.createGym(any(Gym.class), any(UUID.class))).thenReturn(gym1);

                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value(gym1.getName()));

                verify(gymService, times(1)).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return 409 Conflict when gym name already exists for this user")
        void testCreateGymWithDuplicateName() throws Exception {
                when(gymService.createGym(any(Gym.class), any(UUID.class)))
                                .thenThrow(new DuplicateGymException(
                                                "A gym with the name 'Location 1' already exists for this user"));

                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("A gym with the name 'Location 1' already exists for this user"))
                                .andExpect(jsonPath("$.status").value(409))
                                .andExpect(jsonPath("$.error").value("Conflict"));

                verify(gymService, times(1)).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return 422 Unprocessable Entity when name is null")
        void testCreateGymWithNullName() throws Exception {
                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym(null))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name cannot be blank"));

                verify(gymService, never()).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return 422 Unprocessable Entity when name an empty string")
        void testCreateGymWithEmptyStringName() throws Exception {
                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym(""))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.errors", hasSize(2))) // Ensure two errors exist
                                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                                                "Name cannot be blank",
                                                "Name must be between 3 and 50 characters")));

                verify(gymService, never()).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return 422 Unprocessable Entity when name has more than 50 characters")
        void testCreateGymWithTooLongName() throws Exception {
                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym("a".repeat(51)))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(gymService, never()).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return 422 Unprocessable Entity when name has less than 3 characters")
        void testCreateGymWithTooShortName() throws Exception {
                mockMvc.perform(post("/api/gyms/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Gym("a"))))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors").value("Name must be between 3 and 50 characters"));

                verify(gymService, never()).createGym(any(Gym.class), any(UUID.class));
        }

        @Test
        @DisplayName("should return all the gyms from the service and status 200")
        void testFindAllGymsSuccess() throws Exception {
                List<Gym> gyms = Arrays.asList(gym1, gym2);
                when(gymService.findAll()).thenReturn(gyms);

                mockMvc.perform(get("/api/gyms"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Location 1"))
                                .andExpect(jsonPath("$[1].name").value("Location 2"));

                verify(gymService, times(1)).findAll();
        }

        @Test
        @DisplayName("should return an exception and status 500")
        void testFindAllGymsError() throws Exception {
                when(gymService.findAll()).thenThrow(new RuntimeException("Internal server error"));

                mockMvc.perform(get("/api/"))
                                .andExpect(status().isInternalServerError());

                verify(gymService, times(1)).findAll();
        }

        @Test
        @DisplayName("should delete a gym from the repository")
        void testDeleteGymSuccess() throws Exception {
                assertEquals(Status.active, gym1.getStatus(), "Gym should initially be active");
                doAnswer(invocation -> {
                        gym1.setStatus(Status.inactive);
                        return null;
                }).when(gymService).deleteGym(gymId);

                mockMvc.perform(delete("/api/gyms/{id}", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                assertEquals(Status.inactive, gym1.getStatus(), "Gym should finally be inactive");
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
        @DisplayName("should update a existing gym and return status 200")
        void testUpdateGymSuccess() throws Exception {
                when(gymService.updateGym(gymId, gym1.getName())).thenReturn(gym1);
                mockMvc.perform(patch("/api/gyms/{gymId}", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(gym1.getId().toString()))
                                .andExpect(jsonPath("$.name").value(gym1.getName()));
                verify(gymService, times(1)).updateGym(gymId, gym1.getName());
        }

        @Test
        @DisplayName("should return 404 when trying to update a non existing gym")
        void testUpdateGymNotFound() throws Exception {
                doThrow(new GymNotFoundException("Gym with id " + gymId + " not found."))
                                .when(gymService).updateGym(gymId, gym1.getName());
                mockMvc.perform(patch("/api/gyms/{gymId}", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Gym with id " + gymId + " not found."))
                                .andExpect(jsonPath("$.error").value("Not Found"));
                verify(gymService, times(1)).updateGym(gymId, gym1.getName());
        }

        @Test
        @DisplayName("should return 409 when trying to update a gym with name already in use")
        void testUpdateGymNameAlreadyInUse() throws Exception {
                doThrow(new DuplicateGymException("A gym with the name '" + gym1.getName() + "' already exists."))
                                .when(gymService).updateGym(gymId, gym1.getName());
                mockMvc.perform(patch("/api/gyms/{gymId}", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message")
                                                .value("A gym with the name '" + gym1.getName() + "' already exists."))
                                .andExpect(jsonPath("$.error").value("Conflict"));
                verify(gymService, times(1)).updateGym(gymId, gym1.getName());
        }

        @Test
        @DisplayName("should return 422 when trying to update gym with invalid name")
        void testUpdateGymInvalidName() throws Exception {
                Gym invalidGym = new Gym("");

                mockMvc.perform(patch("/api/gyms/{gymId}", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidGym)))
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.message").value("Validation error"))
                                .andExpect(jsonPath("$.errors", hasSize(2)))
                                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                                                "Name cannot be blank",
                                                "Name must be between 3 and 50 characters")));

                verify(gymService, never()).updateGym(any(), any());
        }

        @Test
        @DisplayName("shoud return 404 when trying to update inactive gym")
        void testUpdateGymInactive() throws Exception {
                gym1.setStatus(Status.inactive);
                doThrow(new GymNotFoundException("Gym with id " + gymId + " not found."))
                                .when(gymService).updateGym(gymId, gym1.getName());

                mockMvc.perform(patch("/api/gyms/{gymId}", gymId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(gym1.getName())))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Gym with id " + gymId + " not found."))
                                .andExpect(jsonPath("$.error").value("Not Found"));

                verify(gymService, times(1)).updateGym(gymId, gym1.getName());
        }

        @Test
        @DisplayName("should return status 404 when deleting a inactive gym")
        void testDeleteGymAlreadyDeleted() throws Exception {
                gym1.setStatus(Status.inactive);
                doThrow(new GymNotFoundException("Gym with id " + gymId + " not found."))
                                .when(gymService).deleteGym(gymId);

                mockMvc.perform(delete("/api/gyms/{id}", gymId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                assertEquals(Status.inactive, gym1.getStatus(), "Gym should finally be inactive");
                verify(gymService, times(1)).deleteGym(gymId);
                verifyNoMoreInteractions(gymService);
        }

        @Test
        @DisplayName("should return active gyms for a given user ID")
        void testGetGymsByUserIdSuccess() throws Exception {
                List<Gym> gyms = List.of(gym1, gym2);

                when(gymService.findGymsByUserId(userId)).thenReturn(gyms);

                mockMvc.perform(get("/api/users/{userId}/gyms", userId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("Location 1"))
                                .andExpect(jsonPath("$[1].name").value("Location 2"));

                verify(gymService).findGymsByUserId(userId);
        }

        @Test
        @DisplayName("should return empty list of gyms for a given user ID")
        void testGetGymsByUserIdEmpty() throws Exception {
                when(gymService.findGymsByUserId(userId)).thenReturn(List.of());

                mockMvc.perform(get("/api/users/{userId}/gyms", userId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(gymService).findGymsByUserId(userId);
        }

        @Test
        @DisplayName("should return 404 when user is not found")
        void testGetGymsByUserIdUserNotFound() throws Exception {
                when(gymService.findGymsByUserId(userId))
                                .thenThrow(new UserNotFoundException("User with id " + userId + " not found"));

                mockMvc.perform(get("/api/users/{userId}/gyms", userId))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("User with id " + userId + " not found"));

                verify(gymService).findGymsByUserId(userId);
        }

}