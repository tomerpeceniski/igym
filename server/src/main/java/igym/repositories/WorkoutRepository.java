package igym.repositories;

import igym.entities.enums.Status;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;
import igym.entities.Workout;

/**
 * Repository interface for managing {@link Workout} entities.
 * Provides methods to perform CRUD operations.
 */
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    /**
     * Finds all workouts associated with a given gym.
     *
     * @param gym the gym entity
     * @return a list of workouts belonging to the specified gym
     */
    List<Workout> findByGymAndStatus(Gym gym, Status status);
    Optional<Workout> findByIdAndStatus(UUID id, Status status);
}