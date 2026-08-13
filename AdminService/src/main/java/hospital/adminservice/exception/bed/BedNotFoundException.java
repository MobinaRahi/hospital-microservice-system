package hospital.adminservice.exception.bed;

public class BedNotFoundException extends RuntimeException {
    public BedNotFoundException(String message) { super(message); }
    public static BedNotFoundException byId(Long id) {
        return new BedNotFoundException("Bed with id " + id + " not found");
    }
}
