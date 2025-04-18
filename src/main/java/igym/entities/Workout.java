package igym.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Represents a workout plan which contains multiple exercises.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Workout name is required")
    @Column(nullable = false, length = 50)
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String workoutName;

    @NotEmpty(message = "Workout must contain at least one exercise")
    @OneToMany(cascade = CascadeType.ALL)
    @Valid
    private List<Exercise> exerciseList;
}
