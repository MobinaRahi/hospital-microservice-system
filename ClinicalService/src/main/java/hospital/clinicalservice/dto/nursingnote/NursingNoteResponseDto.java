package hospital.clinicalservice.dto.nursingnote;

import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * DTO for nursing note response data.
 *
 * @author Mobina
 */
public class NursingNoteResponseDto {

    private Long id;

    private Long encounterId;

    private Long patientId;

    private Long nurseId;

    private NoteType noteType;

    private Shift shift;

    private String note;

    private LocalDateTime recordedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
