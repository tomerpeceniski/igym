package igym.dtos;

import igym.entities.enums.Status;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutDTO(
    UUID id,
    String name,
    Status status,
    Instant updated_at,
    UUID gym_id,
    List<ExerciseDTO> exerciseList
) {}

