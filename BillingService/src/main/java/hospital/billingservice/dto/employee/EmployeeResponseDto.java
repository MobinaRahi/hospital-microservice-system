package hospital.billingservice.dto.employee;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.model.enums.EmployeePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning employee data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponseDto {

    private Long id;
    private Long userId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private EmployeePosition position;
    private String department;
    private LocalDate hireDate;
    private BigDecimal baseSalary;
    private String bankAccount;
    private Boolean isActive;
    private long yearsOfService;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
