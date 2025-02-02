package igym.services;

import igym.entities.Gym;
import igym.repositories.GymRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("should throw IllegalArgumentException for a null gym name")
    void testCreateGymWithNullName() {

        assertThatThrownBy(() -> gymService.createGym(new Gym(null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name cannot be null");

        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for an empty gym name")
    void testCreateGymWithEmptyName() {

        assertThatThrownBy(() -> gymService.createGym(new Gym(" ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name cannot be an empty string");

        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for gym name shorter than 3 characters")
    void testCreateGymWithShortName() {

        assertThatThrownBy(() -> gymService.createGym(new Gym("Gy")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name must be between 3 and 50 characters");

        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for gym name longer than 50 characters")
    void testCreateGymWithLongName() {

        String longName = "A".repeat(51);

        assertThatThrownBy(() -> gymService.createGym(new Gym(longName)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name must be between 3 and 50 characters");

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

}