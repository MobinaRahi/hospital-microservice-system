package hospital.clinicalservice.dto.diagnosis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * DTO for diagnosis response data.
 *
 * @author Mobina
 */
public class DiagnosisResponseDto {

    private Long id;

    private Long encounterId;

    private String icd10Code;

    private String name;

    private String description;

    private boolean primary;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
