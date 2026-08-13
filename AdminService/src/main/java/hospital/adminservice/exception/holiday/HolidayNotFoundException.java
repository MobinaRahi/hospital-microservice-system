package hospital.adminservice.exception.holiday;

public class HolidayNotFoundException extends RuntimeException {
    public HolidayNotFoundException(String message) { super(message); }
    public static HolidayNotFoundException byId(Long id) {
        return new HolidayNotFoundException("Holiday with id " + id + " not found");
    }
}
