package igym.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import igym.entities.enums.Status;

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
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @NotBlank(message = "Workout name is required")
    @Column(nullable = false, length = 50)
    @Size(min = 1, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.active;

    @NotEmpty(message = "Workout must contain at least one exercise")
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    @Valid
    @JsonManagedReference
    private List<Exercise> exerciseList;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    @JsonBackReference
    private Gym gym;
}
