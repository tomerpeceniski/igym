package igym.services;

import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import igym.entities.*;
import igym.exceptions.*;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public Gym createGym(Gym gym) {

        if (gymRepository.existsByName(gym.getName())) {
            throw new DuplicateGymException("A gym with the name '" + gym.getName() + "' already exists.");
        }

        return gymRepository.save(gym);
    }

    public Gym updateGym(UUID id, String name) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + id + " not found."));
        if (gymRepository.existsByName(name)) {
            throw new DuplicateGymException("A gym with the name '" + name + "' already exists.");
        }
        gym.setName(name);
        return gymRepository.save(gym);
    }

    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

    public void deleteGym(UUID id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + id + " not found."));

        if (gym.getStatus() == GymStatus.inactive)
            throw new GymAlreadyDeletedException("Gym with id " + id + " is inactive");

        gym.setStatus(GymStatus.inactive);
        gymRepository.save(gym);
    }

}