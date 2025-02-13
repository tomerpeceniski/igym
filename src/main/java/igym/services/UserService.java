package igym.services;

import java.util.List;

import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.repositories.UserRepository;
import igym.services.exceptions.DuplicateUserException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        if (repository.existsByName(user.getName()) == true) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists.");
        }
        return repository.save(user);
    }

}
