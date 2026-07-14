package hospital.clinicalservice.dto.prescription;

import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemResponseDto;
import hospital.clinicalservice.model.enums.PrescriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PrescriptionResponseDto {

    private Long id;

    private Long encounterId;

    private Long patientId;

    private Long doctorId;

    private PrescriptionStatus status;

    private LocalDate prescribedDate;

    private LocalDate expiryDate;

    private String notes;

    private List<PrescriptionItemResponseDto> items = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
