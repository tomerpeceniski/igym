package igym.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import igym.entities.User;
import igym.entities.enums.Status;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Checks if a user with the specified ID exists.
     *
     * @param id the UUID of the user
     * @return true if a user with the given ID exists, false otherwise
     */
    public boolean existsById(@NonNull UUID id);

    /**
     * Checks if a user with the specified name exists with the specified status.
     *
     * @param name the name of the user
     * @return true if a user with the given name exists, false otherwise
     */
    public boolean existsByNameAndStatus(String name, Status status);

    /**
     * Finds all users by their status.
     * @param status
     * @return a list of users with the given status
     */
    List<User> findByStatus(Status status);

    /**
     * Finds a user by their name and status.
     *
     * @param name the name of the user
     * @param status the status of the user
     * @return the user with the given name and status, or null if not found
     */
    Optional<User> findByNameAndStatus(String name, Status status);
}
