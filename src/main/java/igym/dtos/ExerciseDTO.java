package igym.dtos;

import igym.entities.Exercise;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.UUID;

/**
 * A Data Transfer Object (DTO) for exposing exercise data in API responses.
 *
 * <p>
 * Represents a flattened version of the {@link Exercise} entity,
 * containing essential information for each exercise within a workout.
 * </p>
 *
 * @param id         the unique identifier of the exercise
 * @param name       the name of the exercise
 * @param weight     the weight used in this exercise
 * @param numReps    the number of repetitions
 * @param numSets    the number of sets
 * @param note       any additional notes about the exercise
 * @param status     the status (active/inactive) of the exercise
 * @param workout_id the ID of the workout to which this exercise belongs
 * @param updated_at the timestamp of the last update
 */
public record ExerciseDTO(
        UUID id,
        String name,
        double weight,
        int numReps,
        int numSets,
        String note,
        Status status,
        UUID workout_id,
        Instant updated_at) {
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
                exercise.getUpdated_at());
    }
}