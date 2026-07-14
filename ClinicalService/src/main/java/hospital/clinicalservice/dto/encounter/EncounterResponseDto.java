package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.dto.diagnosis.DiagnosisResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.observation.ObservationResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.model.enums.EncounterType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EncounterResponseDto {

    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long appointmentId;

    private Long departmentId;

    private LocalDateTime encounterDate;

    private EncounterType type;

    private EncounterStatus status;

    private String chiefComplaint;

    private String doctorNotes;

    private List<DiagnosisResponseDto> diagnoses = new ArrayList<>();

    private List<PrescriptionResponseDto> prescriptions = new ArrayList<>();

    private List<ObservationResponseDto> observations = new ArrayList<>();

    private List<NursingNoteResponseDto> nursingNotes = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
