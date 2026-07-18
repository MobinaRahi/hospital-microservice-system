package hospital.coreservice.exception.doctor;

/**
 * Thrown when a doctor is not found.
 *
 * @author Mobina
 */
public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
    public static DoctorNotFoundException byId(Long id) {
        return new DoctorNotFoundException("Doctor with id " + id + " not found");
    }
    public static DoctorNotFoundException byUserId(Long userId) {
        return new DoctorNotFoundException("Doctor with userId " + userId + " not found");
    }
    public static DoctorNotFoundException byLicenseNumber(String licenseNumber) {
        return new DoctorNotFoundException("Doctor with licenseNumber " + licenseNumber + " not found");
    }
}
