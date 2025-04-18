package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
}