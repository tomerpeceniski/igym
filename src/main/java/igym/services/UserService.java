package igym.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.exceptions.ObjectNotFoundException;
import igym.repositories.UserRepository;

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
            throw new ObjectNotFoundException("The inserted ID was not found");
        }
        repository.deleteById(id);
    }
}
