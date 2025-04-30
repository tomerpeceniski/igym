package igym.entities;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import igym.entities.enums.Status;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.ToString;

/**
 * Represents a user of the application, who can own multiple gyms.
 * 
 * <p>
 * Each user has a unique identifier, a name, a status (active or inactive),
 * and a list of gyms associated with them. The {@code updated_at} field tracks
 * the last time the user entity was modified.
 * </p>
 *
 * <p>
 * Field validations ensure that the user's name is non-blank and within a
 * specified length.
 * </p>
 */

@ToString(exclude = "gyms")
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Gym> gyms = new ArrayList<>();

    @UpdateTimestamp
    private Instant updated_at;

    public User() {
    }

    public User(String name) {
        this.name = name;
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

    public List<Gym> getGyms() {
        return gyms;
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

}