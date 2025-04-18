package igym.entities;

import java.util.UUID;
import igym.entities.enums.Status;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    @SuppressWarnings("java:S1192")
    @Override
    public String toString() {
        return "{" +
                "id=" + (id != null ? id : "null") +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}