package igym.services;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import igym.entities.Gym;
import igym.repositories.GymRepository;

@DataJpaTest
public class GymServiceTest {

    @Autowired
    GymRepository repository;

    GymService gymService;

    @BeforeEach
    void setUp() {
        gymService = new GymService(repository);
    }

    @Test
    @DisplayName("the service has to return all gyms from the repository")
    void testFindAllGyms() {
        Gym gym1 = new Gym("Profit");
        Gym gym2 = new Gym("Space");
        Gym gym3 = new Gym("Homesplace");
        repository.save(gym1);
        repository.save(gym2);
        repository.save(gym3);

        List<Gym> gyms = gymService.findAllGyms();
        assertThat(gyms).hasSize(3);
        assertThat(gyms).containsExactlyInAnyOrder(gym1, gym2, gym3);

    }

    @Test
    @DisplayName("the service should return an empty list if there are no gyms in the repository")
    void testGymsNotFound() {
        List<Gym> gyms = gymService.findAllGyms();
        assertThat(gyms).isEmpty();
    }

}