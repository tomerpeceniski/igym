package igym.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igym.repositories.GymRepository;

import java.util.List;
import igym.entities.*;

@Service
public class GymService {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Autowired
    public List<Gym> findAllGyms() {
        return gymRepository.findAll();
    }

}
