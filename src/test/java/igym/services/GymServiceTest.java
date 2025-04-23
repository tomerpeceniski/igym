package igym.services;

import igym.entities.Gym;
import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.DuplicateGymException;
import igym.exceptions.GymNotFoundException;
import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private UserService userService;

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private GymService gymService;

    @Test
    @DisplayName("should create a new gym when given a valid name")
    void testCreateGym() {
        Gym gym = new Gym("CrossFit Gym");
        UUID userId = UUID.randomUUID();

        when(userService.findById(any(UUID.class))).thenReturn(new User("Mocked User"));
        when(gymRepository.save(any(Gym.class))).thenReturn(gym);

        Gym result = gymService.createGym(gym, userId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(gym.getName());
        verify(gymRepository, times(1)).save(any(Gym.class));
    }

    @Test
    @DisplayName("should throw DuplicateGymException when attempting to create a gym with an existing name and status active")
    void testAlreadyCreatedGymName() {
        Gym gym = new Gym("CrossFit Gym");
        gym.setStatus(Status.active);
        UUID userId = UUID.randomUUID();

        when(userService.findById(any(UUID.class))).thenReturn(new User("Mocked User"));
        when(gymRepository.existsByNameAndUserIdAndStatus(gym.getName(), userId, Status.active)).thenReturn(true);

        DuplicateGymException exception = assertThrows(
                DuplicateGymException.class,
                () -> gymService.createGym(gym, userId));

        assertEquals("A gym with the name 'CrossFit Gym' already exists for this user", exception.getMessage());
        verify(gymRepository, times(1)).existsByNameAndUserIdAndStatus(gym.getName(), userId, Status.active);
        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should successfully update a gym when provided with a valid new name")
    void testUpdateGym() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        String name = "Updated Gym";
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(gymRepository.existsByNameAndStatus(name, Status.active)).thenReturn(false);
        when(gymRepository.save(any(Gym.class))).thenReturn(gym);
        Gym result = gymService.updateGym(gymId, name);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        verify(gymRepository, times(1)).save(any(Gym.class));
    }

    @Test
    @DisplayName("Should throw GymNotFoundException when attempting to update a gym that does not exist")
    void testUpdateGymNotFound() {
        UUID gymId = UUID.randomUUID();
        String name = "Updated Gym";
        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());
        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> gymService.updateGym(gymId, name));
        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("Should throw DuplicateGymException when attempting to update a gym to a name that is already in use for a active gym")
    void testUpdateGymExistingName() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        String name = "CrossFit Gym";
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(gymRepository.existsByNameAndStatus(name, Status.active)).thenReturn(true);
        DuplicateGymException exception = assertThrows(
                DuplicateGymException.class,
                () -> gymService.updateGym(gymId, name));
        assertEquals("A gym with the name '" + gym.getName() + "' already exists.",
                exception.getMessage());
        verify(gymRepository, times(1)).existsByNameAndStatus(name, Status.active);
        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should return all gyms from the repository")
    void testFindAllGyms() {
        List<Gym> gyms = List.of(new Gym("Gym A"), new Gym("Gym B"), new Gym("Gym C"));

        when(gymRepository.findAll()).thenReturn(gyms);

        List<Gym> result = gymService.findAllGyms();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(gyms);
    }

    @Test
    @DisplayName("the service should return an empty list if there are no gyms in the repository")
    void testGymsNotFound() {
        List<Gym> gyms = gymService.findAllGyms();
        assertThat(gyms).isEmpty();
    }

    @Test
    @DisplayName("should delete a gym from the repository")
    void testDeleteGym() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.active);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(workoutRepository.findByGym(gym)).thenReturn(Collections.emptyList());

        gymService.deleteGym(gymId);

        verify(gymRepository, times(1)).findById(gymId);
        verify(workoutRepository, times(1)).findByGym(gym);
        assertTrue(gym.getStatus() == Status.inactive);
    }

    @Test
    @DisplayName("should throw GymNotFoundException when attempting to delete a gym that does not exist")
    void testDeleteGymNotFound() {
        UUID gymId = UUID.randomUUID();
        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());
        assertThrows(GymNotFoundException.class, () -> {
            gymService.deleteGym(gymId);
        });
        verify(gymRepository, times(1)).findById(gymId);
    }

    @Test
    @DisplayName("should throw GymAlreadyDeletedException when attempting to delete a inactive gym")
    void testDeleteInactiveGym() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.inactive);
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        assertThrows(GymNotFoundException.class, () -> {
            gymService.deleteGym(gymId);
        });
        verify(gymRepository, times(1)).findById(gymId);
    }

    @Test
    @DisplayName("should find a gym by its ID")
    void testFindById() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        Gym result = gymService.findById(gymId);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("CrossFit Gym");
    }

    @Test
    @DisplayName("should throw GymNotFoundException when attempting to find a gym that does not exist")
    void testFindByIdNotFound() {
        UUID gymId = UUID.randomUUID();
        when(gymRepository.findById(gymId)).thenReturn(Optional.empty());
        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> gymService.findById(gymId));
        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
    }

    @Test
    @DisplayName("should throw GymNotFoundException when attempting to find a inactive gym")
    void testFindInactiveGym() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.inactive);
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        GymNotFoundException exception = assertThrows(
                GymNotFoundException.class,
                () -> gymService.findById(gymId));
        assertEquals("Gym with id " + gymId + " not found", exception.getMessage());
    }
}