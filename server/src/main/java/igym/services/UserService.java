package igym.services;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import igym.entities.Gym;
import igym.entities.User;
import igym.entities.enums.Status;
import igym.dtos.LoginResponseDTO;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.exceptions.InvalidCredentialsException;
import igym.exceptions.InvalidPasswordException;
import igym.exceptions.InvalidNameException;
import igym.repositories.UserRepository;
import igym.security.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

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
    private final GymService gymService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository, GymService gymService, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.repository = repository;
        this.gymService = gymService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        logger.info("Fetching all users from the repository");
        List<User> users = repository.findByStatus(Status.active);
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
        deleteGyms(user);
        repository.save(user);
        logger.info("User with id {} inactivated", id);
    }

    private void deleteGyms(User user) {
        List<Gym> gyms = user.getGyms();
        if (gyms != null && !gyms.isEmpty()) {
            gyms.forEach(g -> {
                if (g.getStatus() == Status.active) {
                    gymService.deleteGym(g.getId());
                }
            });
        }
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

        if ((user.getPassword().length() < 6) || (user.getPassword().length() > 20)) {
            throw new InvalidPasswordException("Password must be between 6 and 20 characters");
        }

        if (repository.existsByNameAndStatus(user.getName(), Status.active)) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
     * @throws InvalidNameException   if the provided name is invalid
     */
    public User updateUser(UUID id, String name) {
        logger.info("Attempting to update User with id: {}", id);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        var violations = validator.validateValue(User.class, "name", name);
        if (!violations.isEmpty()) {
            List<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
            throw new InvalidNameException(String.join(", ", messages));
        }

        User user = findById(id);

        if (repository.existsByNameAndStatus(name, Status.active)) {
            throw new DuplicateUserException("A user with the name '" + name + "' already exists.");
        }
        user.setName(name);
        User savedUser = repository.save(
                user);
        logger.info("User with id {} updated successfully", id);
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
        logger.info("User with id {} fetched successfully", id);
        logger.debug("Fetched user: {}", user);
        return user;
    }

    /**
     * Authenticates a user by validating their credentials and generates a JWT
     * token.
     *
     * @param name        the username to authenticate
     * @param rawPassword the raw password to validate
     * @return a LoginResponseDTO containing the JWT token and username
     * @throws UserNotFoundException       if the user is not found or is inactive
     * @throws InvalidCredentialsException if the provided password is incorrect
     */
    public LoginResponseDTO authenticate(String name, String rawPassword) {
        logger.info("Authenticating user with name: {}", name);
        User user = repository.findByNameAndStatus(name, Status.active)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.warn("Authentication failed for user with name: {}", name);
            throw new InvalidCredentialsException("Invalid credentials provided");
        }

        logger.info("User with name {} authenticated successfully", name);
        logger.debug("Authenticated user: {}", user);
        String token = jwtUtil.generateToken(user.getId(), user.getName());
        return new LoginResponseDTO(token, user.getName());
    }

}
