package hospital.clinicalservice.dto.encounter;

import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.model.enums.EncounterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncounterUpdateDto {

    @NotNull(message = "Encounter ID is required for update")
    private Long id;

    private EncounterType type;

    private EncounterStatus status;

    private String chiefComplaint;

    private String doctorNotes;
}
