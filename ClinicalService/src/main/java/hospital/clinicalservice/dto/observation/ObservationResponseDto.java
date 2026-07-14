package hospital.clinicalservice.dto.observation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ObservationResponseDto {

    private Long id;

    private Long encounterId;

    private Long patientId;

    private Long recordedBy;

    private String loincCode;

    private String name;

    private Double valueNumeric;

    private String valueText;

    private String unit;

    private Double referenceRangeLow;

    private Double referenceRangeHigh;

    private boolean abnormal;

    private LocalDateTime observedAt;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
