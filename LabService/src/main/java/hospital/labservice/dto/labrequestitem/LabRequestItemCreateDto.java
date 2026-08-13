package hospital.labservice.dto.labrequestitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new lab request item.
 * Each item represents a specific test to be performed.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code testId} - Lab test ID</li>
 *   <li>{@code testName} - Display name of the test</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabRequestItemCreateDto {

    @NotNull(message = "Test ID is required")
    private Long testId;

    @NotBlank(message = "Test name is required")
    @Size(max = 200, message = "Test name must be at most 200 characters")
    private String testName;
}
