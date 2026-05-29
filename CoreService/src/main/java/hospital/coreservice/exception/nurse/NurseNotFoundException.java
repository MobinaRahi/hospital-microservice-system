package hospital.coreservice.exception.nurse;

public class NurseNotFoundException extends RuntimeException {
    public NurseNotFoundException(String message) {
        super(message);
    }
    public static NurseNotFoundException byId(Long id) {
        return new NurseNotFoundException("Nurse with id " + id + " not found");
    }
}
