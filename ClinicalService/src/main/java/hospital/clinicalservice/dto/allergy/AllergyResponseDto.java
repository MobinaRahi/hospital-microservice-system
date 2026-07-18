package hospital.clinicalservice.dto.allergy;

import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
/**
 * DTO for allergy response data.
 *
 * @author Mobina
 */
public class AllergyResponseDto {

    private Long id;

    private Long patientId;

    private AllergyType type;

    private String allergenName;

    private String reaction;

    private AllergySeverity severity;

    private String symptoms;

    private String notes;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
