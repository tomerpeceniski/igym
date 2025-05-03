package igym.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import igym.dtos.GymDTO;
import igym.entities.*;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.UserNotFoundException;
import igym.services.GymService;
import jakarta.validation.Valid;

/**
 * REST controller for managing gyms.
 * Provides endpoints for creating, retrieving, updating, and deleting gyms.
 */
@RequestMapping(value = "/api")
@RestController
@Validated
public class GymController {

    private final GymService service;

    public GymController(GymService service) {
        this.service = service;
    }

    /**
     * Creates a new gym associated with a user.
     *
     * @param gym    the gym entity to create
     * @param userId the UUID of the user creating the gym
     * @return the created gym (as DTO) and HTTP 201 Created status
     * @throws DuplicateGymException if a gym with the same name already exists
     * @throws UserNotFoundException if the provided userId does not match any user
     */
    // When authentication process is set, change the signature of this method
    @PostMapping(value = "/gyms/{userId}", produces = "application/json")
    public ResponseEntity<GymDTO> createGym(@RequestBody @Valid Gym gym, @PathVariable UUID userId) {
        Gym createdGym = service.createGym(gym, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GymDTO(createdGym));
    }

    /**
     * Updates the name of an existing gym.
     *
     * @param id  the UUID of the gym to update
     * @param gym the gym entity containing the new name
     * @return the updated gym (as DTO) with HTTP 200 OK status
     * @throws GymNotFoundException  if no gym is found with the provided ID
     * @throws DuplicateGymException if a gym with the same new name already exists
     */
    @PatchMapping(value = "/gyms/{id}", produces = "application/json")
    public ResponseEntity<GymDTO> updateGym(@PathVariable("id") UUID id, @RequestBody @Valid Gym gym) {
        String name = gym.getName();
        Gym updatedGym = service.updateGym(id, name);
        return ResponseEntity.ok(new GymDTO(updatedGym));
    }

    /**
     * Retrieves all gyms.
     *
     * @return a list of gyms (as DTO) and HTTP 200 OK status
     */
    @GetMapping(value = "/gyms", produces = "application/json")
    public ResponseEntity<List<GymDTO>> findAllGyms() {
        List<GymDTO> gyms = service.findAllGyms().stream().map(GymDTO::new).toList();
        return ResponseEntity.ok(gyms);
    }

    /**
     * Retrieves all active gyms associated with a specific user.
     *
     * @param userId the ID of the user whose gyms are retrieved
     * @return a list of active gyms belonging to the user
     * @throws UserNotFoundException if the user does not exist or is inactive
     * @throws GymNotFoundException  if the user has no active gyms
     */
    @GetMapping(value = "/users/{userId}/gyms", produces = "application/json")
    public ResponseEntity<List<GymDTO>> getGymsByUserId(@PathVariable UUID userId) {
        List<GymDTO> gyms = service.findGymsByUserId(userId).stream().map(GymDTO::new).toList();
        return ResponseEntity.ok(gyms);
    }

    /**
     * Deletes a gym by its ID.
     *
     * @param id the UUID of the gym to delete
     * @return HTTP 204 No Content status if deletion is successful
     * @throws GymNotFoundException if no gym is found with the provided ID
     */
    @DeleteMapping(value = "/gyms/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteGym(@PathVariable("id") UUID id) {
        service.deleteGym(id);
        return ResponseEntity.noContent().build();
    }

}