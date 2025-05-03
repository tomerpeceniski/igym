package igym.dtos;

import igym.entities.User;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

/**
 * A Data Transfer Object (DTO) for exposing user data in API responses.
 *
 * This DTO provides a simplified, immutable representation of the {@link User} entity,
 * intended for client consumption. It includes only fields relevant for public exposure.
 *
 * @param id         the unique identifier of the user
 * @param name       the name of the user
 * @param status     the current status (active/inactive) of the user
 * @param updated_at the timestamp of the last update
 */
public record UserDTO(
    UUID id,
    String name,
    Status status,
    Instant updated_at
) {
    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getStatus(), user.getUpdated_at());
    }
}