package igym.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import igym.entities.Gym;
import igym.entities.User;
import igym.repositories.UserRepository;
import igym.repositories.GymRepository;

@Configuration
public class TestConfiguration implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GymRepository gymRepository;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User(null, "Maria Brown");
        User user2 = new User(null, "John Snow");

        userRepository.saveAll(Arrays.asList(user1, user2));

        Gym gym1 = new Gym( "gym 1");
        Gym gym2 = new Gym( "gym 2");

        gymRepository.saveAll(Arrays.asList(gym1, gym2));
    }
}
