package igym.services;

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

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public Gym createGym(Gym gym) {

        if (gymRepository.existsByName(gym.getName())) {
            throw new DuplicateGymException("A gym with the name '" + gym.getName() + "' already exists");
        }

        return gymRepository.save(gym);
    }

    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

    public void deleteGym(UUID id) {
        Gym gym = (gymRepository.findById(id))
                .orElseThrow(() -> new GymNotFoundException("Gym with id " + id + " not found"));

        if (gym.getStatus() == Status.inactive)
            throw new GymNotFoundException("Gym with id " + id + " not found");

        gym.setStatus(Status.inactive);
        gymRepository.save(gym);
    }

}