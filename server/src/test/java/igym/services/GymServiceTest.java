package igym.services;

import igym.entities.Exercise;
import igym.entities.Gym;
import igym.entities.User;
import igym.entities.Workout;
import igym.entities.enums.Status;
import igym.exceptions.DuplicateGymException;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.UserNotFoundException;
import igym.repositories.GymRepository;
import igym.repositories.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private GymService gymService;

    @Test
    @DisplayName("should create a new gym when given a valid name")
    void testCreateGym() {
        Gym gym = new Gym("CrossFit Gym");
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User("Mocked User")));
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

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User("Mocked User")));
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
        User mockedUser = new User("MockedUser");
        gym.setUser(mockedUser);
        String name = "Updated Gym";
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(gymRepository.existsByNameAndUserIdAndStatus(name, mockedUser.getId(), Status.active)).thenReturn(false);
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
        User mockedUser = new User("MockedUser");
        gym.setUser(mockedUser);
        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(gymRepository.existsByNameAndUserIdAndStatus(name, mockedUser.getId(), Status.active)).thenReturn(true);
        DuplicateGymException exception = assertThrows(
                DuplicateGymException.class,
                () -> gymService.updateGym(gymId, name));
        assertEquals("A gym with the name '" + gym.getName() + "' already exists for this user",
                exception.getMessage());
        verify(gymRepository, times(1)).existsByNameAndUserIdAndStatus(name, mockedUser.getId(), Status.active);
        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    @DisplayName("should return all active gyms from the repository")
    void testFindAllGyms() {
        List<Gym> gyms = List.of(new Gym("Gym A"), new Gym("Gym B"), new Gym("Gym C"));

        when(gymRepository.findByStatus(Status.active)).thenReturn(gyms);

        List<Gym> result = gymService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(gyms);
    }

    @Test
    @DisplayName("the service should return an empty list if there are no gyms in the repository or all are inactive")
    void testFindAllGymsEmpty() {
        Gym gym = new Gym("Gym A");
        ReflectionTestUtils.setField(gym, "id", UUID.randomUUID());
        gym.setStatus(Status.inactive);

        when(gymRepository.findByStatus(Status.active)).thenReturn(List.of());

        List<Gym> result = gymService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should delete a gym and inactivate workouts and their exercises")
    void testDeleteGymAndInactivateWorkouts() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.active);

        Exercise exercise = new Exercise();
        exercise.setName("Exercise 1");
        ReflectionTestUtils.setField(exercise, "id", UUID.randomUUID());
        exercise.setStatus(Status.active);

        Workout workout = new Workout();
        workout.setName("Workout 1");
        ReflectionTestUtils.setField(workout, "id", UUID.randomUUID());
        workout.setStatus(Status.active);
        workout.setExerciseList(List.of(exercise));
        Workout workoutInactive = new Workout();
        workoutInactive.setStatus(Status.inactive);
        gym.setWorkouts(List.of(workout, workoutInactive));

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));

        doAnswer(invocation -> {
            workout.setStatus(Status.inactive);
            workout.getExerciseList().forEach(e -> e.setStatus(Status.inactive));
            return null;
        }).when(workoutService).deleteWorkout(workout.getId());

        gymService.deleteGym(gymId);

        assertEquals(Status.inactive, gym.getStatus());
        assertEquals(Status.inactive, workout.getStatus());
        assertEquals(Status.inactive, exercise.getStatus());

        verify(gymRepository).findById(gymId);
    }

    @Test
    @DisplayName("should delete a gym when its workout list is empty")
    void testDeleteGymEmptyWorkouts() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.active);
        gym.setWorkouts(List.of());

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));

        gymService.deleteGym(gymId);

        assertEquals(Status.inactive, gym.getStatus());

        verify(gymRepository).findById(gymId);
    }

    @Test
    @DisplayName("should delete a gym when its workout list is null")
    void testDeleteGymNullWorkouts() {
        UUID gymId = UUID.randomUUID();
        Gym gym = new Gym("CrossFit Gym");
        ReflectionTestUtils.setField(gym, "id", gymId);
        gym.setStatus(Status.active);
        gym.setWorkouts(null);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));

        gymService.deleteGym(gymId);

        assertEquals(Status.inactive, gym.getStatus());

        verify(gymRepository).findById(gymId);
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
    @DisplayName("should throw GymNotFoundException when attempting to delete a inactive gym")
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

    @Test
    @DisplayName("should return gyms when user exists and gyms are active")
    void testFindGymsByUserId() {
        UUID userId = UUID.randomUUID();
        User user = new User("Test User");
        Gym gym1 = new Gym("Gym 1");
        Gym gym2 = new Gym("Gym 2");
        Gym gym3 = new Gym("Gym 3");
        gym3.setStatus(Status.inactive);
        List<Gym> gyms = List.of(gym1, gym2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gymRepository.findByUserIdAndStatus(userId, Status.active)).thenReturn(gyms);

        List<Gym> result = gymService.findGymsByUserId(userId);

        assertThat(result).hasSize(2).containsExactlyElementsOf(gyms);
        verify(userRepository).findById(userId);
        verify(gymRepository).findByUserIdAndStatus(userId, Status.active);
    }

    @Test
    @DisplayName("should return empty list of gyms when user exists but has no active gyms")
    void testFindEmptyGymsByUserId() {
        UUID userId = UUID.randomUUID();
        User user = new User("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gymRepository.findByUserIdAndStatus(userId, Status.active)).thenReturn(List.of());

        List<Gym> result = gymService.findGymsByUserId(userId);

        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
        verify(gymRepository).findByUserIdAndStatus(userId, Status.active);
    }

    @Test
    @DisplayName("should throw UserNotFoundException when user is not found when trying to find gym for this user id")
    void testFindGymsByUserId_userNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenThrow(new UserNotFoundException("User with id " + userId + " not found"));

        assertThrows(UserNotFoundException.class, () -> gymService.findGymsByUserId(userId));

        verify(userRepository).findById(userId);
        verify(gymRepository, never()).findByUserIdAndStatus(any(), any());
    }

}