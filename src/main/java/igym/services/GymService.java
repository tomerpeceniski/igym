package igym.services;

import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import igym.entities.*;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public Gym createGym(Gym gym) {
        if (gym.getName() == null)
            throw new IllegalArgumentException("Name cannot be null");
        if (gym.getName().trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be an empty string");
        if (gym.getName().length() < 3 || gym.getName().length() > 50) {
            throw new IllegalArgumentException("Name must be between 3 and 50 characters");
        }
        return gymRepository.save(gym);
    }

    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

}