package igym.controllers;

import org.springframework.web.bind.annotation.RestController;

import igym.dtos.UserDTO;
import igym.dtos.LoginRequestDTO;
import igym.dtos.LoginResponseDTO;
import igym.entities.User;
import igym.exceptions.DuplicateUserException;
import igym.exceptions.UserNotFoundException;
import igym.services.UserService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import igym.dtos.UpdateUserNameDTO;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
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
    @GetMapping("/users")
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
    @DeleteMapping("/users/{id}")
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
    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
        User savedUser = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(savedUser));
    }

    /**
     * Updates the name of an existing user.
     *
     * @param id   the UUID of the user to update
     * @param dto the DTO containing the new name
     * @return the updated user (as DTO) with HTTP 200 OK status
     * @throws UserNotFoundException  if no user is found with the provided ID
     * @throws DuplicateUserException if a user with the same new name already exists
     * @throws InvalidNameException if the provided name is invalid
     */
    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @RequestBody @Valid UpdateUserNameDTO dto) {
        User updatedUser = service.updateUser(id, dto.name());
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param request the login request containing username and password
     * @return a LoginResponseDTO containing the JWT token and username
     * @throws InvalidCredentialsException if the credentials are invalid
     * @throws UserNotFoundException if the user is not found or is inactive
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = service.authenticate(request.name(), request.password());
        return ResponseEntity.ok(response);
    }
}
