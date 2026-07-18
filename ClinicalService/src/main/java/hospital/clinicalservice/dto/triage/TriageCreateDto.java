package hospital.clinicalservice.dto.triage;

import hospital.clinicalservice.model.enums.TriageLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for creating a new triage assessment.
 *
 * @author Mobina
 */
public class TriageCreateDto {

    @NotNull(message = "Encounter ID is required")
    private Long encounterId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Triage level is required")
    private TriageLevel level;

    @NotBlank(message = "Chief complaint is required")
    @Size(max = 1000, message = "Chief complaint must not exceed 1000 characters")
    private String chiefComplaint;

    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters")
    private String symptoms;

    @Size(max = 1000, message = "Blood pressure must not exceed 1000 characters")
    private String bloodPressure;

    private Long heartRate;

    private Long temperature;

    private String consciousness;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    @NotNull(message = "Triaged by (user ID) is required")
    private Long triagedBy;
}
