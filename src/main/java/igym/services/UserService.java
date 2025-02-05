package igym.services;

import java.util.List;

import org.springframework.stereotype.Service;

import igym.entities.User;
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

}
