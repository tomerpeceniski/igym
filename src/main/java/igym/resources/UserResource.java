package igym.resources;

import org.springframework.web.bind.annotation.RestController;

import igym.entities.UserEntity;
import igym.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        List<UserEntity> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }
    
}
