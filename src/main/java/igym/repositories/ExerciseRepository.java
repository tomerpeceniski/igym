package igym.repositories;

import igym.entities.Exercise;
import igym.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Exercise entities.
 * This interface extends JpaRepository to provide CRUD operations and custom
 * query methods.
 */
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    /**
     * Retrieves an active exercise by its ID.
     *
     * @param id     the UUID of the exercise
     * @param status the status to filter by (usually {@code Status.active})
     * @return an {@code Optional} containing the exercise if found, or empty if not
     *         found
     */
    Optional<Exercise> findByIdAndStatus(UUID id, Status status);
}
