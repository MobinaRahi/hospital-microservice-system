package hospital.adminservice.exception.bed;

public class BedNotAvailableException extends RuntimeException {
    public BedNotAvailableException(Long id, String status) {
        super("Bed " + id + " is not available. Current status: " + status);
    }
}
