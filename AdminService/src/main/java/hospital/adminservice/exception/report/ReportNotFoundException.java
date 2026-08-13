package hospital.adminservice.exception.report;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(String message) { super(message); }
    public static ReportNotFoundException byId(Long id) {
        return new ReportNotFoundException("Report with id " + id + " not found");
    }
}
