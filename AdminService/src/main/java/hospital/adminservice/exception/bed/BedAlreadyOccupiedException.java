package hospital.adminservice.exception.bed;

public class BedAlreadyOccupiedException extends RuntimeException {
    public BedAlreadyOccupiedException(Long id) {
        super("Bed " + id + " is already occupied by a patient");
    }
}
