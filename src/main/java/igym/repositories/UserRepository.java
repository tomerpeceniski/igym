package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import igym.entities.User;
import igym.entities.enums.Status;

public interface UserRepository extends JpaRepository<User, UUID> {
    public boolean existsById(@NonNull UUID id);

    public boolean existsByNameAndStatus(String name, Status status);
}
