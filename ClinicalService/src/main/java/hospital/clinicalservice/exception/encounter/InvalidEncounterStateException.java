package hospital.clinicalservice.exception.encounter;

public class InvalidEncounterStateException extends RuntimeException {

    private InvalidEncounterStateException(String message) {
        super(message);
    }

    public static InvalidEncounterStateException alreadyCompleted(Long id) {
        return new InvalidEncounterStateException("Encounter " + id + " is already completed");
    }

    public static InvalidEncounterStateException alreadyCancelled(Long id) {
        return new InvalidEncounterStateException("Encounter " + id + " is already cancelled");
    }

    public static InvalidEncounterStateException cannotComplete(Long id, String currentStatus) {
        return new InvalidEncounterStateException("Encounter " + id + " cannot be completed from status: " + currentStatus);
    }

    public static InvalidEncounterStateException cannotCancel(Long id, String currentStatus) {
        return new InvalidEncounterStateException("Encounter " + id + " cannot be cancelled from status: " + currentStatus);
    }
}
