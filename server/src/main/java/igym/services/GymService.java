package igym.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
import igym.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import igym.entities.*;
import igym.entities.enums.Status;
import igym.exceptions.*;

/**
 * Service class responsible for managing gym operations,
 * handling business logic, and interacting with the repository layer.
 *
 * <p>
 * This service receives requests from the controller layer, applies necessary
 * validations
 * and transformations, and persists or retrieves gym-related data.
 * </p>
 */
@Service
public class GymService {

    private static final Logger logger = LoggerFactory.getLogger(GymService.class);
    private final GymRepository gymRepository;
    private final WorkoutService workoutService;
    private final UserRepository userRepository;

    public GymService(GymRepository gymRepository, WorkoutService workoutService,
            UserRepository userRepository) {
        this.gymRepository = gymRepository;
        this.workoutService = workoutService;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new gym associated with a user.
     *
     * @param gym    the gym entity to be saved
     * @param userId the UUID of the user who owns the gym
     * @return the saved gym entity
     * @throws UserNotFoundException if the user does not exist or is inactive
     * @throws DuplicateGymException if a gym with the same name already exists for
     *                               the user
     */
    @Transactional
    public Gym createGym(Gym gym, UUID userId) {
        logger.info("Attempting to create a new gym");
        logger.debug("Gym creation request with values: {}", gym);

        User user = findUserById(userId);
        gym.setUser(user);

        if (gymRepository.existsByNameAndUserIdAndStatus(gym.getName(), userId, Status.active)) {
            throw new DuplicateGymException("A gym with the name '" + gym.getName() + "' already exists for this user");
        }

        Gym savedGym = gymRepository.save(gym);
        logger.info("New gym created with id {}", savedGym.getId());
        logger.debug("New gym persisted: {}", savedGym);
        return savedGym;
    }

    /**
     * Updates the name of an existing gym.
     *
     * @param id   the UUID of the gym to update
     * @param name the new name for the gym
     * @return the updated gym entity
     * @throws DuplicateGymException if a gym with the same new name already exists
     * @throws GymNotFoundException  if the gym does not exist or is inactive
     */
    public Gym updateGym(UUID id, String name) {
        logger.info("Attempting to update Gym with id: {}", id);
        Gym gym = findById(id);

        if (gymRepository.existsByNameAndUserIdAndStatus(name, gym.getUser().getId(), Status.active)) {
            throw new DuplicateGymException("A gym with the name '" + name + "' already exists for this user");
        }

        gym.setName(name);
        Gym savedGym = gymRepository.save(gym);

        logger.info("Gym with id {} updated sucessfully", id);
        logger.debug("Updated Gym persisted: {}", savedGym);

        return savedGym;
    }

    /**
     * Retrieves all gyms in the system.
     *
     * @return a list of all gyms
     */
    public List<Gym> findAll() {
        logger.info("Fetching all gyms from the repository");
        List<Gym> gyms = gymRepository.findByStatus(Status.active);
        logger.info("Found {} gyms", gyms.size());
        logger.debug("Fetched gyms: {}", gyms);
        return gyms;
    }

    /**
     * Performs a logical deletion (inactivation) of a gym by setting its status to
     * {@code Status.inactive}.
     *
     * @param id the UUID of the gym to inactivate
     * @throws GymNotFoundException if the gym does not exist or is already inactive
     */
    @Transactional
    public void deleteGym(UUID id) {
        logger.info("Attempting to inactivate gym with id {}", id);
        Gym gym = findById(id);

        deleteWorkouts(gym);
        gym.setStatus(Status.inactive);
        gymRepository.save(gym);
        logger.info("Gym with id {} inactivated", id);
    }

    private void deleteWorkouts(Gym gym) {
        List<Workout> workouts = gym.getWorkouts();
        if (workouts != null && !workouts.isEmpty()) {
            workouts.forEach(w -> {
                if (w.getStatus() == Status.active) {
                    workoutService.deleteWorkout(w.getId());
                }
            });
        }
    }

    /**
     * Retrieves a gym by its ID, ensuring it is active.
     *
     * @param id the UUID of the gym
     * @return the found gym entity
     * @throws GymNotFoundException if the gym does not exist or is inactive
     */
    public Gym findById(UUID id) {
        logger.info("Fetching gym with id: {}", id);

        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + id + " not found"));
        if (gym.getStatus() == Status.inactive) {
            logger.warn("Gym with id {} is inactive", id);
            throw new GymNotFoundException("Gym with id " + id + " not found");
        }

        logger.debug("Fetched gym: {}", gym);
        return gym;
    }

       /**
     * Retrieves a gym by its {@link User} ID, ensuring it is active.
     *
     * @param userId the UUID of the owner {@link User}
     * @return the found gym entity
     * @throws UserNotFoundException if the User does not exist or is inactive
     */
    public List<Gym> findGymsByUserId(UUID userId) {
        logger.info("Fetching gyms for user with id: {}", userId);
    
        // validating existence of user with id
        findUserById(userId);
    
        List<Gym> gyms = gymRepository.findByUserIdAndStatus(userId, Status.active);
    
        logger.info("Found {} active gyms for user {}", gyms.isEmpty() ? 0 : gyms.size(), userId);
        return gyms;
    }
    
    private User findUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        if (user.getStatus() == Status.inactive) {
            logger.warn("User with id {} is inactive", userId);
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        return user;
    }

}