package igym.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
import igym.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import igym.entities.*;
import igym.entities.enums.Status;
import igym.exceptions.*;

@Service
public class GymService {

    private static final Logger logger = LoggerFactory.getLogger(GymService.class);
    private final GymRepository gymRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    public GymService(GymRepository gymRepository, UserService userService, WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
        this.gymRepository = gymRepository;
        this.userService = userService;
    }

    @Transactional
    public Gym createGym(Gym gym, UUID userId) {
        logger.info("Attempting to create a new gym");
        logger.debug("Gym creation request with values: {}", gym);

        User user = userService.findById(userId);
        gym.setUser(user);

        if (gymRepository.existsByNameAndUserIdAndStatus(gym.getName(), userId, Status.active)) {
            throw new DuplicateGymException("A gym with the name '" + gym.getName() + "' already exists for this user");
        }

        Gym savedGym = gymRepository.save(gym);
        logger.info("New gym created with id {}", savedGym.getId());
        logger.debug("New gym persisted: {}", savedGym);
        return savedGym;
    }

    public Gym updateGym(UUID id, String name) {
        logger.info("Attempting to update Gym with id: {}", id);

        Gym gym = findById(id);
        if (gym.getStatus() == Status.inactive) {
            logger.warn("Gym with id {} is inactive", id);
            throw new GymNotFoundException("Gym with id " + id + " not found");
        }

        if (gymRepository.existsByNameAndStatus(name, Status.active)) {
            throw new DuplicateGymException("A gym with the name '" + name + "' already exists.");
        }

        gym.setName(name);
        Gym savedGym = gymRepository.save(gym);

        logger.info("Gym with id {} updated sucessfully", id);
        logger.debug("Updated Gym persisted: {}", savedGym);

        return savedGym;
    }

    public List<Gym> findAllGyms() {
        logger.info("Fetching all gyms from the repository");
        List<Gym> gyms = gymRepository.findAll();
        logger.info("Found {} gyms", gyms.size());
        logger.debug("Fetched gyms: {}", gyms);
        return gyms;
    }

    public void deleteGym(UUID id) {
        logger.info("Attempting to inactivate gym with id {}", id);
        Gym gym = findById(id);

        if (gym.getStatus() == Status.inactive) {
            throw new GymNotFoundException("Gym with id " + id + " not found");
        }

        inactivateWorkouts(gym);
        gym.setStatus(Status.inactive);
        gymRepository.save(gym);
        logger.info("Gym with id {} inactivated", id);
    }

    private void inactivateWorkouts(Gym gym) {
        List<Workout> workouts = workoutRepository.findByGym(gym);
        if (workouts.stream().anyMatch(w -> w.getStatus() == Status.active)) {
            logger.info("Inactivating {} workouts for gym {}", workouts.size(), gym.getId());
        }

        workouts.forEach(workout -> {
            if (workout.getStatus() == Status.active) {
                workout.setStatus(Status.inactive);
                if (workout.getExerciseList() != null) {
                    workout.getExerciseList().forEach(ex -> {
                        ex.setStatus(Status.inactive);
                        logger.debug("Exercise {} inactivated", ex.getId());
                    });
                }
                logger.debug("Workout {} inactivated", workout.getId());
            }
        });

        workoutRepository.saveAll(workouts); // Save all updated workouts
    }

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

}