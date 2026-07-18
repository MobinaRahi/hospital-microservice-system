package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.model.enums.EncounterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new encounter (visit).
 *
 * <p><strong>When to use:</strong> When a patient starts a visit with a doctor.</p>
 * <p><strong>Required fields:</strong> patientId, doctorId, type</p>
 * <p><strong>Optional fields:</strong> appointmentId, departmentId, chiefComplaint, doctorNotes</p>
 *
 * @author Mobina
 */
@Getter
@Setter
public class EncounterCreateDto {

    /**
     * Patient ID from CoreService.
     * Required - identifies which patient is visiting.
     */
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    /**
     * Doctor ID from CoreService.
     * Required - identifies which doctor is seeing the patient.
     */
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    /**
     * Appointment ID from CoreService (optional).
     * Links this encounter to a pre-booked appointment.
     * Null if the patient walked in without an appointment.
     */
    private Long appointmentId;

    /**
     * Department ID from CoreService (optional).
     * The department where the encounter takes place.
     */
    private Long departmentId;

    /**
     * Type of encounter:
     * - OUTPATIENT: Patient visits clinic and goes home
     * - INPATIENT: Patient is admitted to hospital
     * - EMERGENCY: Urgent visit to ER
     * - FOLLOW_UP: Follow-up visit after previous encounter
     */
    @NotNull(message = "Encounter type is required")
    private EncounterType type;

    /**
     * Patient's chief complaint - the main reason for the visit.
     * Example: "chest pain for 2 days", "headache and dizziness"
     * Max 2000 characters.
     */
    private String chiefComplaint;

    /**
     * Doctor's notes about the encounter.
     * General observations, treatment plan, follow-up instructions.
     * Max 2000 characters.
     */
    private String doctorNotes;
}
