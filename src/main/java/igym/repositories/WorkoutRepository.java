package igym.repositories;

import igym.entities.enums.Status;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;
import igym.entities.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    List<Workout> findByGym(Gym gym);
    Optional<Workout> findByIdAndStatus(UUID id, Status status);
}