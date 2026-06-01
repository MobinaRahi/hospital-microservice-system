package hospital.coreservice.exception.nurse;

public class NurseNotFoundException extends RuntimeException {
    public NurseNotFoundException(String message) {
        super(message);
    }
    public static NurseNotFoundException byId(Long id) {
        return new NurseNotFoundException("Nurse with id " + id + " not found");
    }
    public static NurseNotFoundException byUserId(Long userId) {
        return new NurseNotFoundException("Nurse with id " + userId + " not found");
    }
    public static NurseNotFoundException byNurseCode(String nurseCode) {
        return new NurseNotFoundException("Nurse with code " + nurseCode + " not found");
    }
    public static NurseNotFoundException byPhone(String phone) {
        return new NurseNotFoundException("Phone number " + phone + " not found");
    }
    public static NurseNotFoundException byNationalId(String nationalId) {
        return new NurseNotFoundException("National id " + nationalId + " not found");
    }
}
