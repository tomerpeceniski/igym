package igym.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import igym.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
