package igym.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Represents a workout plan which contains multiple exercises.
 * Lombok's @Setter is applied to the class, but the ID field
 * uses @Setter(AccessLevel.NONE)
 * to prevent accidental assignment, as it's auto-generated.
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
    @Setter(AccessLevel.NONE)
    private UUID id;

    @NotBlank(message = "Workout name is required")
    @Column(nullable = false, length = 50)
    @Size(min = 1, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotEmpty(message = "Workout must contain at least one exercise")
    @OneToMany(cascade = CascadeType.ALL)
    @Valid
    private List<Exercise> exerciseList;
}
