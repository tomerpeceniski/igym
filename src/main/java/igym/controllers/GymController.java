package igym.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import igym.entities.*;
import igym.services.GymService;

@RequestMapping(value = "/api")
@RestController
public class GymController {

    @Autowired
    private GymService service;

    @GetMapping(value = "/gyms", produces = "application/json")
    public ResponseEntity<List<Gym>> findAllGyms() {
        List<Gym> gyms = service.findAllGyms();

        return ResponseEntity.ok(gyms);
    }

}