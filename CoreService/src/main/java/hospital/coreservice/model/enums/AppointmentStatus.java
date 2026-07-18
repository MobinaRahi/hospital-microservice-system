package hospital.coreservice.model.enums;

/**
 * Status of an appointment:
 * SCHEDULED → CHECK_IN → IN_PROGRESS → COMPLETED / CANCELLED / NO_SHOW
 *
 * @author Mobina
 */
public enum AppointmentStatus {
    SCHEDULED,
    CHECK_IN,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW,
}
