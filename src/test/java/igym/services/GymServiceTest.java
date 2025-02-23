package igym.services;

import igym.entities.Gym;
import igym.exceptions.DuplicateGymException;
import igym.exceptions.GymNotFoundException;
import igym.repositories.GymRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @InjectMocks
    private GymService gymService;

    @Test
    @DisplayName("should create a new gym when given a valid name")
    void testCreateGym() {

        Gym gym = new Gym("CrossFit Gym");

        when(gymRepository.save(any(Gym.class))).thenReturn(gym);

        Gym result = gymService.createGym(gym);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("CrossFit Gym");
        verify(gymRepository, times(1)).save(any(Gym.class));
    }

    @Test
    @DisplayName("should throw DuplicateGymException when attempting to create a gym with a duplicate name")
    void testAlreadyCreatedGymName() {

        Gym gym = new Gym("CrossFit Gym");

        when(gymRepository.existsByName(gym.getName())).thenReturn(true);

        DuplicateGymException exception = assertThrows(
                DuplicateGymException.class,
                () -> gymService.createGym(gym));

        assertEquals("A gym with the name 'CrossFit Gym' already exists.", exception.getMessage());
        verify(gymRepository, times(1)).existsByName(gym.getName());
        verify(gymRepository, never()).save(gym);
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
        gym.setId(gymId);
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));

        gymService.deleteGym(gymId);

        verify(gymRepository, times(1)).findById(gymId);
        verify(gymRepository, times(1)).delete(gym);

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

}