package hospital.clinicalservice.model.enums;

/**
 * وضعیت ویزیت
 */
public enum EncounterStatus {
    SCHEDULED,
    IN_PROGRESS,    // در حال ویزیت
    COMPLETED,      // تکمیل شده
    CANCELLED       // لغو شده
}
