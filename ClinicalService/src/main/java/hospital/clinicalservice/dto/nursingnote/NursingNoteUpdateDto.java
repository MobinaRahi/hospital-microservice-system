package hospital.clinicalservice.dto.nursingnote;

import hospital.clinicalservice.model.enums.NoteType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NursingNoteUpdateDto {

    @NotNull(message = "Note ID is required for update")
    private Long id;

    private NoteType noteType;

    @Size(max = 2000, message = "Note must not exceed 2000 characters")
    private String note;
}
