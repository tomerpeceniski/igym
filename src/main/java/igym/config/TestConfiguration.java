package igym.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import igym.entities.UserEntity;
import igym.repositories.UserRepository;

@Configuration
public class TestConfiguration implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user1 = new UserEntity("Maria Brown");
        UserEntity user2 = new UserEntity("John Snow");

        userRepository.saveAll(Arrays.asList(user1, user2));
    }
}
