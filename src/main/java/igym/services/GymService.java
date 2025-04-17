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

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public Gym createGym(Gym gym) {
        logger.info("Attempting to create new gym with name {}", gym.getName());
        logger.debug("Gym creation request with values: {}", gym);
        if (gymRepository.existsByName(gym.getName())) {
            throw new DuplicateGymException("A gym with the name '" + gym.getName() + "' already exists");
        }
        Gym savedGym = gymRepository.save(gym);
        logger.info("New gym created with id {}", savedGym.getId());
        logger.debug("New gym persisted: {}", savedGym);
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
        Gym gym = (gymRepository.findById(id))
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + id + " not found"));

        if (gym.getStatus() == Status.inactive)
            throw new GymNotFoundException("Gym with id " + id + " not found");

        gym.setStatus(Status.inactive);
        gymRepository.save(gym);
        logger.info("Gym with id {} inactivated", id);
    }
    
}