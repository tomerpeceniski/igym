package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
