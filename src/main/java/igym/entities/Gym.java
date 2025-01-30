package igym.entities;

import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    public Gym(String name) {
        setName(name);
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException("Name must be between 3 and 50 characters");
        }
        this.name = name;
    }
}
