package hospital.coreservice.exception.doctor;

import com.hospital.coreService.exception.department.DepartmentNotFoundException;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }

    public static DoctorNotFoundException byId(Long id) {
        return new DoctorNotFoundException("Doctor with id " + id + " not found");
    }
}
