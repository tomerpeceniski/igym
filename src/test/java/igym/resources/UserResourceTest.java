package igym.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import igym.entities.UserEntity;
import igym.services.UserService;

@SpringBootTest
public class UserResourceTest {

    @Autowired
    UserResource resource;

    @Autowired
    UserService service;

    @Test
    public void findAllTest() {
        List<UserEntity> listUsers = service.findAll();

        ResponseEntity<List<UserEntity>> response = resource.findAll();
        assertEquals(listUsers, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
