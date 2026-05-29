package hospital.coreservice.exception.patient;

public class DuplicateNationalIdException extends RuntimeException {
    public DuplicateNationalIdException( String nationalId) {
        super("nationalId already exists : "+ nationalId);
    }
}
