package hospital.labservice.dto.sample;

import hospital.labservice.model.enums.SampleQuality;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing sample.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code labRequestId}, {@code sampleNumber}, {@code sampleType}
 * and {@code collectionDate} are not updatable as they define the sample identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleUpdateDto {

    private SampleQuality quality;

    @Size(max = 100, message = "Collection site must be at most 100 characters")
    private String collectionSite;

    @Size(max = 100, message = "Container type must be at most 100 characters")
    private String containerType;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
