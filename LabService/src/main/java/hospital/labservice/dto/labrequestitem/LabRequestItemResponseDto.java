package hospital.labservice.dto.labrequestitem;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.dto.labresult.LabResultResponseDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.model.enums.RequestItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning lab request item data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabRequestItemResponseDto {

    private Long id;
    private Long testId;
    private String testName;
    private RequestItemStatus status;
    private LabTestResponseDto test;
    private LabResultResponseDto result;
}
