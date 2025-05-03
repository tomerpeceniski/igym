package igym.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;
import igym.entities.enums.Status;

/**
 * Repository interface for managing {@link Gym} entities.
 * Provides methods to perform CRUD operations.
 */
public interface GymRepository extends JpaRepository<Gym, UUID> {
    
    /**
     * Checks if a gym with the specified name exists.
     *
     * @param name the name of the gym
     * @return true if a gym with the given name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Checks if a gym with the specified name exists for a particular user with a particular status.
     *
     * @param name   the name of the gym
     * @param userId the ID of the user
     * @return true if a gym with the given name exists for the user, false
     *         otherwise
     */
    boolean existsByNameAndUserIdAndStatus(String name, UUID userId, Status status);

    /**
     * finds all gyms by its status
     * @param status
     * @return a list of gyms with the given status
     */
    List<Gym> findByStatus(Status status);

    List<Gym> findByUserIdAndStatus(UUID userId, Status status);
}