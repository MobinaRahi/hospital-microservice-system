package hospital.coreservice.exception.patient;

public class PatientNotFoundException extends RuntimeException {

    private PatientNotFoundException(String message) {
        super(message);
    }

    public static PatientNotFoundException byId(Long id) {
        return new PatientNotFoundException("Patient with id " + id + " not found");
    }

    public static PatientNotFoundException byNationalId(String nationalId) {
        return new PatientNotFoundException("Patient with nationalId " + nationalId + " not found");
    }

    public static PatientNotFoundException byPhoneNumber(String phoneNumber) {
        return new PatientNotFoundException("Patient with phoneNumber " + phoneNumber + " not found");
    }
}