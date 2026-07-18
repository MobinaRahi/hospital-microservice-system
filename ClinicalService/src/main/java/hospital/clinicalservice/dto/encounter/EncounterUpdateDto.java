package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.model.enums.EncounterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing encounter.
 *
 * <p><strong>When to use:</strong> When doctor updates encounter details during or after visit.</p>
 * <p><strong>Immutable fields:</strong> patientId, doctorId, appointmentId, encounterDate</p>
 * <p><strong>Mutable fields:</strong> type, status, chiefComplaint, doctorNotes</p>
 *
 * @author Mobina
 */
@Getter
@Setter
public class EncounterUpdateDto {

    /**
     * Encounter ID (required for update).
     * Used to identify which encounter to update.
     */
    @NotNull(message = "Encounter ID is required for update")
    private Long id;

    /**
     * Updated encounter type (optional).
     * Only provide if changing the type.
     */
    private EncounterType type;

    /**
     * Updated encounter status (optional).
     * Use complete/cancel endpoints for status changes instead.
     */
    private EncounterStatus status;

    /**
     * Updated chief complaint (optional).
     * Only provide if changing the complaint.
     */
    private String chiefComplaint;

    /**
     * Updated doctor notes (optional).
     * Only provide if changing the notes.
     */
    private String doctorNotes;
}
