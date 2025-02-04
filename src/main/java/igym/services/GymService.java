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
        return gymRepository.save(gym);
    }

    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

}