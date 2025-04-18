package igym.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents an exercise within a workout.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Optional if you want to create it with all fields
@Builder            // Optional if you want to use builder pattern
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
}
