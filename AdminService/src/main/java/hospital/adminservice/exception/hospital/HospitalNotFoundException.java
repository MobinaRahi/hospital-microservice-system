package hospital.adminservice.exception.hospital;

/**
 * Exception thrown when a hospital is not found.
 *
 * @author MobinaRahi
 */
public class HospitalNotFoundException extends RuntimeException {

    public HospitalNotFoundException(String message) {
        super(message);
    }

    public static HospitalNotFoundException byId(Long id) {
        return new HospitalNotFoundException("Hospital with id " + id + " not found");
    }

    public static HospitalNotFoundException byCode(String code) {
        return new HospitalNotFoundException("Hospital with code '" + code + "' not found");
    }
}
