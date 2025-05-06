package igym.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import igym.entities.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.ToString;

/**
 * Represents a gym owned by a user.
 * 
 * <p>
 * Each gym has a unique identifier, a name, a status (active or inactive),
 * a timestamp of the last update, and a reference to its owner {@link User}.
 * It also maintains a list of workouts associated with it.
 * </p>
 *
 * <p>
 * Field validations ensure that the gym's name is non-blank and within a
 * specified length.
 * </p>
 */

@ToString()
@Table(name = "gyms")
@Entity
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.active;

    @UpdateTimestamp
    private Instant updated_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Workout> workouts;

    public Gym(String name) {
        this.name = name;
    }

    public Gym() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

}