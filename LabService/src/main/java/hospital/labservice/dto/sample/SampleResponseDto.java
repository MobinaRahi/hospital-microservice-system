package hospital.labservice.dto.sample;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning sample data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleResponseDto {

    private Long id;
    private Long labRequestId;
    private String sampleNumber;
    private SampleType sampleType;
    private LocalDateTime collectionDate;
    private Long collectedBy;
    private String collectionSite;
    private String containerType;
    private SampleQuality quality;
    private LocalDateTime receivedAtLab;
    private Long receivedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
