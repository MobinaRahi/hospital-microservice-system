package hospital.clinicalservice.dto.nursingnote;

import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NursingNoteCreateDto {

    private Long encounterId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Nurse ID is required")
    private Long nurseId;

    @NotNull(message = "Note type is required")
    private NoteType noteType;

    private Shift shift;

    @NotBlank(message = "Note content is required")
    @Size(max = 2000, message = "Note must not exceed 2000 characters")
    private String note;
}
