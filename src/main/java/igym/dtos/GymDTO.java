package igym.dtos;

import igym.entities.Gym;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

public record GymDTO(
    UUID id,
    String name,
    Status status,
    Instant updated_at,
    UUID user_id
) {
        public GymDTO(Gym gym) {
        this(gym.getId(), gym.getName(), gym.getStatus(), gym.getUpdated_at(), gym.getUser().getId());
    }
}