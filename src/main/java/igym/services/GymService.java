package igym.services;

import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import igym.entities.*;
import igym.exceptions.DuplicateGymException;

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

    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

    public void deleteGym(Gym gym) {
        gymRepository.deleteById(gym.getId());
    }

}