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

import igym.entities.*;
import igym.exceptions.GymNotFoundException;
import igym.exceptions.UserNotFoundException;
import igym.services.GymService;
import jakarta.validation.Valid;

@RequestMapping(value = "/api")
@RestController
@Validated
public class GymController {

    private final GymService service;

    public GymController(GymService service) {
        this.service = service;
    }

    // When authentication process is set, change the signature of this method
    @PostMapping(value = "/gyms/{userId}", produces = "application/json")
    public ResponseEntity<Gym> createGym(@RequestBody @Valid Gym gym, @PathVariable UUID userId) {
        Gym createdGym = service.createGym(gym, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGym);
    }

    @PatchMapping(value = "/gyms/{id}", produces = "application/json")
    public ResponseEntity<Gym> updateGym(@PathVariable("id") UUID id, @RequestBody @Valid Gym gym) {
        String name = gym.getName();
        Gym updatedGym = service.updateGym(id, name);
        return ResponseEntity.ok(updatedGym);
    }

    @GetMapping(value = "/gyms", produces = "application/json")
    public ResponseEntity<List<Gym>> findAllGyms() {
        List<Gym> gyms = service.findAllGyms();

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
    public ResponseEntity<List<Gym>> getGymsByUserId(@PathVariable UUID userId) {
        List<Gym> gyms = service.findGymsByUserId(userId);
        return ResponseEntity.ok(gyms);
    }

    @DeleteMapping(value = "/gyms/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteGym(@PathVariable("id") UUID id) {
        service.deleteGym(id);
        return ResponseEntity.noContent().build();
    }

}