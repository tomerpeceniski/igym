package igym.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;
import igym.entities.enums.Status;

public interface GymRepository extends JpaRepository<Gym, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndUserId(String name, UUID userId);

    List<Gym> findByUserIdAndStatus(UUID userId, Status status);
}