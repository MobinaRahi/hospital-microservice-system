package hospital.coreservice.exception.nurse;

public class NurseAlreadyExistsException extends RuntimeException {
    public NurseAlreadyExistsException(String message) {
        super(message);
    }

    public static NurseAlreadyExistsException byUserId(Long userId) {
        return new  NurseAlreadyExistsException("Nurse already exists for userId: " + userId);
    }

    public static NurseAlreadyExistsException byNationalId(String nationalId) {
        return new NurseAlreadyExistsException("Nurse already exists for national: "+nationalId);
    }
    public static NurseAlreadyExistsException byNurseCode(String nurseCode) {
        return new NurseAlreadyExistsException("Nurse already exists for nurseCode: "+nurseCode);
    }
}
