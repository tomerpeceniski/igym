package igym.entities.enums;

import igym.entities.*;

/**
 * Enumeration representing the possible status values for entities like {@link User}, {@link Gym}, {@link Workout}, and {@link Exercise}.
 * 
 * <ul>
 *   <li>{@code active} - The entity is currently active and available for use.</li>
 *   <li>{@code inactive} - The entity is logically deleted or disabled, but still exists in the database.</li>
 * </ul>
 */
public enum Status {
    active,
    inactive
}
