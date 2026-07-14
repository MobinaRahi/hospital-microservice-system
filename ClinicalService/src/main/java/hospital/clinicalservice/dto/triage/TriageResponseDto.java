package hospital.clinicalservice.dto.triage;

import hospital.clinicalservice.model.enums.TriageLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TriageResponseDto {

    private Long id;

    private Long encounterId;

    private Long patientId;

    private TriageLevel level;

    private String chiefComplaint;

    private String symptoms;

    private String bloodPressure;

    private Long heartRate;

    private Long temperature;

    private String consciousness;

    private String notes;

    private Long triagedBy;

    private LocalDateTime triagedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
