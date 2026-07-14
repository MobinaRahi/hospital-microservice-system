package hospital.clinicalservice.dto.prescriptionitem;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrescriptionItemResponseDto {

    private Long id;

    private Long prescriptionId;

    private Long drugId;

    private String drugName;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;

    private Integer quantity;

    private boolean substitutionAllowed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
