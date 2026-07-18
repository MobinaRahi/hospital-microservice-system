package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.model.enums.EncounterType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for encounter response data.
 *
 * <p><strong>When returned:</strong> In all GET endpoints for encounters.</p>
 * <p><strong>Includes:</strong> All encounter fields + related diagnoses, prescriptions, observations, nursing notes.</p>
 *
 * @author Mobina
 */
@Getter
@Setter
public class EncounterResponseDto {

    /** Unique encounter ID */
    private Long id;

    /** Patient ID from CoreService */
    private Long patientId;

    /** Doctor ID from CoreService */
    private Long doctorId;

    /** Appointment ID from CoreService (null if walk-in) */
    private Long appointmentId;

    /** Department ID from CoreService */
    private Long departmentId;

    /** Date and time when the encounter started */
    private LocalDateTime encounterDate;

    /** Type: OUTPATIENT, INPATIENT, EMERGENCY, FOLLOW_UP */
    private EncounterType type;

    /** Status: IN_PROGRESS, COMPLETED, CANCELLED */
    private EncounterStatus status;

    /** Patient's chief complaint */
    private String chiefComplaint;

    /** Doctor's notes */
    private String doctorNotes;

    /** List of diagnoses (ICD-10) */
    private List<DiagnosisResponseDto> diagnoses = new ArrayList<>();

    /** List of prescriptions */
    private List<PrescriptionResponseDto> prescriptions = new ArrayList<>();

    /** List of observations (vital signs) */
    private List<ObservationResponseDto> observations = new ArrayList<>();

    /** List of nursing notes */
    private List<NursingNoteResponseDto> nursingNotes = new ArrayList<>();

    /** Record creation timestamp */
    private LocalDateTime createdAt;

    /** Last modification timestamp */
    private LocalDateTime updatedAt;
}
