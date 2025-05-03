package igym.dtos;

import igym.entities.Workout;
import igym.entities.enums.Status;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Data Transfer Object (DTO) for exposing workout details in API responses.
 *
 * <p>
 * Represents a concise view of a {@link Workout}, including its
 * basic information and a list of related {@link ExerciseDTO}s.
 * </p>
 *
 * @param id           the unique identifier of the workout
 * @param name         the name of the workout
 * @param status       the current status (active/inactive) of the workout
 * @param updated_at   the timestamp of the last update
 * @param gym_id       the unique identifier of the gym associated with this
 *                     workout
 * @param exerciseList the list of exercises in this workout
 */
public record WorkoutDTO(
        UUID id,
        String name,
        Status status,
        Instant updated_at,
        UUID gym_id,
        List<ExerciseDTO> exerciseList) {
    public WorkoutDTO(Workout workout) {
        this(
                workout.getId(),
                workout.getName(),
                workout.getStatus(),
                workout.getUpdated_at(),
                workout.getGym().getId(),
                workout.getExerciseList().stream()
                        .filter(ex -> ex.getStatus() == Status.active)
                        .map(ExerciseDTO::new)
                        .collect(Collectors.toList()));
    }
}