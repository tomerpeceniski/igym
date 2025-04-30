package igym.dtos;

import igym.entities.Workout;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record WorkoutDTO(
    UUID id,
    String name,
    Status status,
    Instant updated_at,
    UUID gym_id,
    List<ExerciseDTO> exerciseList
) {
        public WorkoutDTO(Workout workout) {
        this(
            workout.getId(),
            workout.getName(),
            workout.getStatus(),
            workout.getUpdated_at(),
            workout.getGym().getId(),
            workout.getExerciseList().stream()
                .map(ExerciseDTO::new)
                .collect(Collectors.toList())
        );
    }
}