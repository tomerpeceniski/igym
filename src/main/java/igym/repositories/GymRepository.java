package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;

public interface GymRepository extends JpaRepository<Gym, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndUserId(String name, UUID userId);

    
}