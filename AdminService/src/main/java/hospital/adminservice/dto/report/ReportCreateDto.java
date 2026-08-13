package hospital.adminservice.dto.report;

import hospital.adminservice.model.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO for creating a new report generation request.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Report name (max 200 characters)</li>
 *   <li>{@code type} - Report type (PATIENT_STATS, FINANCIAL_MONTHLY, etc.)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code parameters} - Report parameters as JSON string</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {
 *   "name": "Monthly Revenue Report - March 2026",
 *   "type": "FINANCIAL_MONTHLY",
 *   "parameters": "{\"startDate\": \"2026-03-01\", \"endDate\": \"2026-03-31\"}"
 * }
 * </pre>
 *
 * @author MobinaRahi
 */
@Data
@SuperBuilder
public class ReportCreateDto {

    @NotBlank(message = "Report name is required")
    @Size(max = 200, message = "Report name must be at most 200 characters")
    private String name;

    @NotNull(message = "Report type is required")
    private ReportType type;

    /**
     * Report parameters stored as JSON string.
     * Example: {"startDate": "2026-03-01", "endDate": "2026-03-31"}
     */
    private String parameters;
}
