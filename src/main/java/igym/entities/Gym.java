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

@ToString(exclude = "user")
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
    private User user;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    @JsonManagedReference
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

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

}