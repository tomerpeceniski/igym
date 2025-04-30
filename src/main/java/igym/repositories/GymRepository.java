package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.Gym;

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
     * Checks if a gym with the specified name exists for a particular user.
     *
     * @param name   the name of the gym
     * @param userId the ID of the user
     * @return true if a gym with the given name exists for the user, false
     *         otherwise
     */
    boolean existsByNameAndUserId(String name, UUID userId);

}