package igym.repositories;

import igym.entities.Exercise;
import igym.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Exercise entities.
 * This interface extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    /**
     * Checks if an exercise with the specified name exists.
     *
     * @param name the name of the exercise
     * @return true if an exercise with the given name exists, false otherwise
     */
    Optional<Exercise> findByIdAndStatus(UUID id, Status status);
}
