package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.model.enums.EncounterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncounterCreateDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long appointmentId;

    private Long departmentId;

    @NotNull(message = "Encounter type is required")
    private EncounterType type;

    private String chiefComplaint;

    private String doctorNotes;
}
