package igym.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * Represents an exercise within a workout.
 */
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Exercise name is required")
    @Column(nullable = false, length = 50)
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @PositiveOrZero(message = "Weight must be zero or positive")
    private double weight;

    @Min(value = 1, message = "Reps must be at least 1")
    private int numReps;

    @Min(value = 1, message = "Sets must be at least 1")
    private int numSets;

    private String note;

    public Exercise() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public int getNumReps() {
        return numReps;
    }
    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }
    public int getNumSets() {
        return numSets;
    }
    public void setNumSets(int numSets) {
        this.numSets = numSets;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
