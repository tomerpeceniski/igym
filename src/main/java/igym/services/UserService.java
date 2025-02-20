package igym.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.exceptions.ObjectNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.repositories.UserRepository;
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

    public void deleteUser(UUID id) {
        if(!repository.existsById(id)) {
            throw new ObjectNotFoundException("There is no User with ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public User createUser(User user) {
        if (repository.existsByName(user.getName())) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists.");
        }
        return repository.save(user);
    }

}
