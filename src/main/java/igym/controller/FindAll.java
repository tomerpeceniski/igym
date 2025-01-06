package igym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import igym.entities.*;
import igym.services.GymService;

@RequestMapping(value = "api/gyms")
@RestController
public class FindAll {

    @Autowired
    private GymService service;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Gym>> findAllGyms() {
        try {
            List<Gym> gyms = service.findAllGyms();
            if (gyms.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gyms);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
