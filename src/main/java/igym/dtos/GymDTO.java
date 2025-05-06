package igym.dtos;

import igym.entities.Gym;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

/**
 * A Data Transfer Object (DTO) for exposing gym information in API responses.
 *
 * <p>
 * Represents a simplified, read-only version of the {@link Gym} entity,
 * containing only essential information and the user ID that owns the gym.
 * </p>
 *
 * @param id         the unique identifier of the gym
 * @param name       the name of the gym
 * @param status     the status (active/inactive) of the gym
 * @param updated_at the timestamp of the last update
 * @param user_id    the unique identifier of the user who owns this gym
 */
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