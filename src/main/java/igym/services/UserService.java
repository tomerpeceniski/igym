package igym.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(UUID id) {
        Optional<User> optional = repository.findById(id);
        return optional.orElseThrow(() -> new ObjectNotFoundException("ID: ", id));
    }

    public User insert(User obj) {
        return repository.save(obj);
    }

}
