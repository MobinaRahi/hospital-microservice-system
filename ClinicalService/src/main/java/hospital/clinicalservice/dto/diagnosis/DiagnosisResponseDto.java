package hospital.clinicalservice.dto.diagnosis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
