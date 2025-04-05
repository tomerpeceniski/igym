package igym.services;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        return users;
    }

    @Transactional
    public void deleteUser(UUID id) {
        logger.info("Attempting to inactivate user with id {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        if (user.getStatus() == Status.inactive) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        user.setStatus(Status.inactive);
        repository.save(user);
        logger.info("User with id {} inactivated", id);
    }

    @Transactional
    public User createUser(User user) {
        logger.info("Attempting to create new user with username {}", user.getName());
        if (repository.existsByName(user.getName())) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists");
        }
        User savedUser = repository.save(user);
        logger.info("New user created with id {}", savedUser.getId());
        return savedUser;
    }

}
