package igym.repositories;

import igym.entities.Gym;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GymRepositoryTest {

    @Autowired
    private GymRepository repository;

    @Test
    @DisplayName("should return all the gyms from the repository")
    public void findAllGymsSavedTest() {
        Gym gym1 = new Gym("Profit");
        Gym gym2 = new Gym("Space");
        Gym gym3 = new Gym("Homesplace");
        repository.save(gym1);
        repository.save(gym2);
        repository.save(gym3);

        List<Gym> gyms = repository.findAll();
    
        assertThat(gyms).hasSize(3); 
        assertThat(gyms).containsExactlyInAnyOrder(gym1, gym2, gym3); 
    }

    @Test
    @DisplayName("should return an empty list if there are no gyms in the repository")
    public void findAllGymsEmptyRepoTest() {
        List<Gym> gyms = repository.findAll();
        assertThat(gyms).isEmpty();
    }
}