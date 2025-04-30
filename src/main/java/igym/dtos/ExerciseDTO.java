package igym.dtos;

import igym.entities.Exercise;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

public record ExerciseDTO(
    UUID id,
    String name,
    double weight,
    int numReps,
    int numSets,
    String note,
    Status status,
    UUID workout_id,
    Instant updated_at
) {
        public ExerciseDTO(Exercise exercise) {
        this(
            exercise.getId(),
            exercise.getName(),
            exercise.getWeight(),
            exercise.getNumReps(),
            exercise.getNumSets(),
            exercise.getNote(),
            exercise.getStatus(),
            exercise.getWorkout().getId(),
            exercise.getUpdated_at()
        );
    }
}