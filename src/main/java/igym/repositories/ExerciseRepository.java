package igym.repositories;

import igym.entities.Exercise;
import igym.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    Optional<Exercise> findByIdAndStatus(UUID id, Status status);
}
