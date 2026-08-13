package hospital.labservice.dto.sample;

import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating a new sample.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code labRequestId} - Parent lab request ID</li>
 *   <li>{@code sampleNumber} - Unique sample number</li>
 *   <li>{@code sampleType} - Type of biological sample</li>
 *   <li>{@code collectionDate} - Date/time of sample collection</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code collectedBy} - User ID who collected the sample</li>
 *   <li>{@code collectionSite} - Location where sample was collected</li>
 *   <li>{@code containerType} - Type of collection container</li>
 *   <li>{@code quality} - Sample quality assessment</li>
 *   <li>{@code notes} - Additional notes about the sample</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleCreateDto {

    @NotNull(message = "Lab request ID is required")
    private Long labRequestId;

    @NotBlank(message = "Sample number is required")
    @Size(max = 50, message = "Sample number must be at most 50 characters")
    private String sampleNumber;

    @NotNull(message = "Sample type is required")
    private SampleType sampleType;

    @NotNull(message = "Collection date is required")
    private LocalDateTime collectionDate;

    private Long collectedBy;

    @Size(max = 100, message = "Collection site must be at most 100 characters")
    private String collectionSite;

    @Size(max = 100, message = "Container type must be at most 100 characters")
    private String containerType;

    private SampleQuality quality;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
