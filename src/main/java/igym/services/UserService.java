package igym.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igym.entities.User;
import igym.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository rep;

    public List<User> findAll() {
        return rep.findAll();
    }

}
