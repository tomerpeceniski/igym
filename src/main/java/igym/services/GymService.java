package igym.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
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

    public GymService(GymRepository gymRepository, UserService userService) {
        this.gymRepository = gymRepository;
        this.userService = userService;
    }

    @Transactional
    public Gym createGym(Gym gym, UUID userId) {
        logger.info("Attempting to create a new gym");
        logger.debug("Gym creation request with values: {}", gym);

        User user = userService.findById(userId);
        gym.setUser(user);
        
        if(gymRepository.existsByNameAndUserId(gym.getName(), userId)) {
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
        if (gymRepository.existsByName(name)) {
            throw new DuplicateGymException("A gym with the name '" + name + "' already exists.");
        }
        if (gym.getStatus() == Status.inactive) {
            logger.warn("Gym with id {} is inactive", id);
            throw new GymNotFoundException("Gym with id " + id + " not found");
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
            logger.warn("Gym with id {} is inactive", id);
            throw new GymNotFoundException("Gym with id " + id + " not found");
        }

        gym.setStatus(Status.inactive);
        gymRepository.save(gym);
        logger.info("Gym with id {} inactivated", id);
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

    public List<Gym> findGymsByUserId(UUID userId) {
        logger.info("Fetching gyms for user with id: {}", userId);
    
        // validating existence of user with id
        userService.findById(userId);
    
        List<Gym> gyms = gymRepository.findByUserIdAndStatus(userId, Status.active);
    
        logger.info("Found {} active gyms for user {}", gyms.isEmpty() ? 0 : gyms.size(), userId);
        return gyms;
    }
}