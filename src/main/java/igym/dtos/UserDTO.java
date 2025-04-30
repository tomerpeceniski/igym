package igym.dtos;

import igym.entities.User;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

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