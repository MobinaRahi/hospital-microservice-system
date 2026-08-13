package hospital.adminservice.exception.hospital;

public class DuplicateHospitalCodeException extends RuntimeException {
    public DuplicateHospitalCodeException(String code) {
        super("Hospital with code '" + code + "' already exists");
    }
}
