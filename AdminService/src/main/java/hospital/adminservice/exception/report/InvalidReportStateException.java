package hospital.adminservice.exception.report;

public class InvalidReportStateException extends RuntimeException {
    public InvalidReportStateException(Long id, String currentStatus, String attemptedAction) {
        super("Report " + id + " is in state " + currentStatus + " and cannot be " + attemptedAction);
    }
}
