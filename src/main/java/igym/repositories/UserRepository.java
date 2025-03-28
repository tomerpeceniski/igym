package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import igym.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    public boolean existsById(@NonNull UUID id);

    public boolean existsByName(String name);
}
