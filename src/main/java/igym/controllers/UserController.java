package igym.controllers;

import org.springframework.web.bind.annotation.RestController;

import igym.dtos.UserDTO;
import igym.entities.User;
import igym.exceptions.DuplicateUserException;
import igym.exceptions.UserNotFoundException;
import igym.services.UserService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of users (as DTO) with HTTP 200 OK status
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = service.findAll().stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(list);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the UUID of the user to delete
     * @return HTTP 204 No Content status if deletion is successful
     * @throws UserNotFoundException if no user is found with the provided ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a new user.
     *
     * @param user the user entity to create
     * @return the created user (as DTO) and HTTP 201 Created status
     * @throws DuplicateUserException if a user with the same name already exists
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
        User savedUser = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(savedUser));
    }

    /**
     * Updates the name of an existing user.
     *
     * @param id   the UUID of the user to update
     * @param user the user entity containing the new name
     * @return the updated user (as DTO) with HTTP 200 OK status
     * @throws UserNotFoundException  if no user is found with the provided ID
     * @throws DuplicateUserException if a user with the same new name already exists
     */
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @RequestBody @Valid User user) {
        String name = user.getName();
        User updatedUser = service.updateUser(id, name);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }
}
