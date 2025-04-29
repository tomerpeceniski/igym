package igym.services;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import igym.entities.Gym;
import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.repositories.UserRepository;
import jakarta.transaction.Transactional;

/**
 * Service class responsible for managing user operations,
 * handling business logic, and interacting with the repository layer.
 *
 * <p>
 * This service receives requests from the controller layer, applies necessary
 * validations
 * and transformations, and persists or retrieves user-related data.
 * </p>
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        logger.info("Fetching all users from the repository");
        List<User> users = repository.findAll();
        logger.info("Found {} users", users.size());
        logger.debug("Fetched users: {}", users);
        return users;
    }

    /**
     * Performs a logical deletion (inactivation) of a user and their associated
     * gyms.
     *
     * @param id the UUID of the user to inactivate
     * @throws UserNotFoundException if the user does not exist or is already
     *                               inactive
     */
    @Transactional
    public void deleteUser(UUID id) {
        logger.info("Attempting to inactivate user with id {}", id);
        User user = findById(id);
        user.setStatus(Status.inactive);
        inactivateGyms(user);
        repository.save(user);
        logger.info("User with id {} inactivated", id);
    }

    private void inactivateGyms(User user) {
        List<Gym> gyms = user.getGyms();
        if (gyms.stream().anyMatch(g -> g.getStatus() == Status.active))
            logger.info("Inactivating {} gyms for user {}", gyms.size(), user.getId());

        gyms.forEach(gym -> {
            if (gym.getStatus() == Status.active) {
                gym.setStatus(Status.inactive);
                logger.debug("Gym {} inactivated", gym.getId());
            }
        });
    }

    /**
     * Creates a new user.
     *
     * @param user the user entity to be saved
     * @return the saved user entity
     * @throws DuplicateUserException if a user with the same name already exists
     */
    @Transactional
    public User createUser(User user) {
        logger.info("Attempting to create a new user");
        logger.debug("User creation request with values: {}", user);
        if (repository.existsByName(user.getName())) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists");
        }
        User savedUser = repository.save(user);
        logger.info("New user created with id {}", savedUser.getId());
        logger.debug("New user persisted: {}", savedUser);
        return savedUser;
    }

    /**
     * Updates the name of an existing user.
     *
     * @param id   the UUID of the user to update
     * @param name the new name for the user
     * @return the updated user entity
     * @throws DuplicateUserException if a user with the same new name already
     *                                exists
     * @throws UserNotFoundException  if the user does not exist
     */
    public User updateUser(UUID id, String name) {
        logger.info("Attempting to update User with id: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
        if (repository.existsByName(name)) {
            throw new DuplicateUserException("A user with the name '" + name + "' already exists.");
        }
        user.setName(name);
        User savedUser = repository.save(user);
        logger.info("User with id {} updated sucessfully", id);
        logger.debug("Updated User persisted: {}", savedUser);
        return savedUser;
    }

    /**
     * Retrieves a user by their ID, ensuring they are active.
     *
     * @param id the UUID of the user
     * @return the found user entity
     * @throws UserNotFoundException if the user does not exist or is inactive
     */
    public User findById(UUID id) {
        logger.info("Fetching user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        if (user.getStatus() == Status.inactive) {
            logger.warn("User with id {} is inactive", id);
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        logger.debug("Fetched user: {}", user);
        return user;
    }

}
