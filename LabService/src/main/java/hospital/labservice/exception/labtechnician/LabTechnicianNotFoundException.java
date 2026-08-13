package hospital.labservice.exception.labtechnician;

/**
 * Exception thrown when a lab technician is not found.
 *
 * @author MobinaRahi
 */
public class LabTechnicianNotFoundException extends RuntimeException {

    public LabTechnicianNotFoundException(String message) {
        super(message);
    }

    public static LabTechnicianNotFoundException byId(Long id) {
        return new LabTechnicianNotFoundException("Lab technician with id " + id + " not found");
    }

    public static LabTechnicianNotFoundException byUserId(Long userId) {
        return new LabTechnicianNotFoundException("Lab technician with user id " + userId + " not found");
    }

    public static LabTechnicianNotFoundException byEmployeeCode(String employeeCode) {
        return new LabTechnicianNotFoundException("Lab technician with employee code '" + employeeCode + "' not found");
    }
}
