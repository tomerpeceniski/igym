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

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        logger.info("Fetching all users from the repository");
        List<User> users = repository.findAll();
        logger.info("Found {} users", users.size());
        logger.debug("Fetched users: {}", users);
        return users;
    }

    @Transactional
    public void deleteUser(UUID id) {
        logger.info("Attempting to inactivate user with id {}", id);
        User user = findById(id);
        if (user.getStatus() == Status.inactive) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        user.setStatus(Status.inactive);
        inactivateGyms(user);
        repository.save(user);
        logger.info("User with id {} inactivated", id);
    }

    private void inactivateGyms(User user) {
        List<Gym> gyms = user.getGyms();
        if(gyms.stream().anyMatch(g -> g.getStatus() == Status.active)) logger.info("Inactivating {} gyms for user {}", gyms.size(), user.getId());

        gyms.forEach(gym -> {
            if (gym.getStatus() == Status.active) {
                gym.setStatus(Status.inactive);
                logger.debug("Gym {} inactivated", gym.getId());
            }
        });
    }

    @Transactional
    public User createUser(User user) {
        logger.info("Attempting to create a new user");
        logger.debug("User creation request with values: {}", user);
        if (repository.existsByNameAndStatus(user.getName(), Status.active)) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists");
        }
        User savedUser = repository.save(user);
        logger.info("New user created with id {}", savedUser.getId());
        logger.debug("New user persisted: {}", savedUser);
        return savedUser;
    }

    public User updateUser(UUID id, String name) {
        logger.info("Attempting to update User with id: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

        if (user.getStatus() == Status.inactive) {
            logger.warn("User with id {} is inactive", id);
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        
        if (repository.existsByNameAndStatus(name, Status.active)) {
            logger.warn("A user with the name '{}' already exists", name);
            throw new DuplicateUserException("A user with the name '" + name + "' already exists.");
        }
        user.setName(name);
        User savedUser = repository.save(user);
        logger.info("User with id {} updated sucessfully", id);
        logger.debug("Updated User persisted: {}", savedUser);
        return savedUser;
    }

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
