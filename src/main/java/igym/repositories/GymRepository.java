package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;
import igym.entities.enums.Status;

public interface GymRepository extends JpaRepository<Gym, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndUserIdAndStatus(String name, UUID userId, Status status);

    
}