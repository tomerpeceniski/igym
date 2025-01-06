package igym.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Table(name = "gym")
@Entity
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

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
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }
}
