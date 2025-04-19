package igym.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import igym.entities.enums.Status;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.ToString;

@ToString
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
}