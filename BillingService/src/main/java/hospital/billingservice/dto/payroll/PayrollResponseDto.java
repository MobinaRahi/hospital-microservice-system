package hospital.billingservice.dto.payroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.dto.employee.EmployeeResponseDto;
import hospital.billingservice.model.enums.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning payroll data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayrollResponseDto {

    private Long id;
    private EmployeeResponseDto employee;
    private Integer month;
    private Integer year;
    private BigDecimal baseSalary;
    private BigDecimal overtime;
    private BigDecimal bonuses;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDate paymentDate;
    private PayrollStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
