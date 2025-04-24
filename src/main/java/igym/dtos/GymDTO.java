package igym.dtos;

import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

public record GymDTO(
    UUID id,
    String name,
    Status status,
    Instant updated_at,
    UUID user_id
) {}
