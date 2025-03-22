package igym.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.entities.enums.Status;
import igym.exceptions.UserNotFoundException;
import igym.exceptions.DuplicateUserException;
import igym.exceptions.UserAlreadyDeletedException;
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

    @Transactional
    public void deleteUser(UUID id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
        if(user.getStatus() == Status.inactive) {  
            throw new UserAlreadyDeletedException("Gym with id " + id + " is already inactive");
        }
        user.setStatus(Status.inactive);
        repository.save(user);
    }

    @Transactional
    public User createUser(User user) {
        if (repository.existsByName(user.getName())) {
            throw new DuplicateUserException("An user with the name " + user.getName() + " already exists.");
        }
        return repository.save(user);
    }

}
