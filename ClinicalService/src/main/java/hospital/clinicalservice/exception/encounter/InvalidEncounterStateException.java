package hospital.clinicalservice.exception.encounter;

/**
 * Exception thrown when an encounter state transition is invalid.
 *
 * <p><strong>When thrown:</strong></p>
 * <ul>
 *   <li>Trying to complete an already completed encounter</li>
 *   <li>Trying to cancel an already cancelled encounter</li>
 *   <li>Trying to complete a cancelled encounter</li>
 * </ul>
 *
 * <p><strong>HTTP Status:</strong> 400 Bad Request</p>
 *
 * @author Mobina
 */
public class InvalidEncounterStateException extends RuntimeException {

    private InvalidEncounterStateException(String message) {
        super(message);
    }

    /**
     * Thrown when trying to complete an already completed encounter.
     * Example: PATCH /encounters/123/complete → "Encounter 123 is already completed"
     */
    public static InvalidEncounterStateException alreadyCompleted(Long id) {
        return new InvalidEncounterStateException("Encounter " + id + " is already completed");
    }

    /**
     * Thrown when trying to cancel an already cancelled encounter.
     * Example: PATCH /encounters/123/cancel → "Encounter 123 is already cancelled"
     */
    public static InvalidEncounterStateException alreadyCancelled(Long id) {
        return new InvalidEncounterStateException("Encounter " + id + " is already cancelled");
    }

    /**
     * Thrown when trying to complete an encounter that is in an invalid state.
     * Example: Trying to complete a CANCELLED encounter.
     */
    public static InvalidEncounterStateException cannotComplete(Long id, String currentStatus) {
        return new InvalidEncounterStateException("Encounter " + id + " cannot be completed from status: " + currentStatus);
    }

    /**
     * Thrown when trying to cancel an encounter that is in an invalid state.
     * Example: Trying to cancel a COMPLETED encounter.
     */
    public static InvalidEncounterStateException cannotCancel(Long id, String currentStatus) {
        return new InvalidEncounterStateException("Encounter " + id + " cannot be cancelled from status: " + currentStatus);
    }
}
