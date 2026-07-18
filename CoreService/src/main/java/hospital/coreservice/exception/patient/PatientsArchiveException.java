package hospital.coreservice.exception.patient;
import lombok.Getter;
import java.util.List;

@Getter
/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientsArchiveException extends RuntimeException {
    private final List<Long> failedIds;

    public PatientsArchiveException(List<Long> failedIds) {
        super("Failed to archive patients with IDs: " + failedIds);
        this.failedIds = failedIds;
    }

}
