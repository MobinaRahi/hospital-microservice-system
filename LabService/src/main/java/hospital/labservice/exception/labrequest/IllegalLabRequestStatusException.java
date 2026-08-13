package hospital.labservice.exception.labrequest;

/**
 * Exception thrown for illegal lab request status transitions.
 *
 * @author MobinaRahi
 */
public class IllegalLabRequestStatusException extends RuntimeException {

    public IllegalLabRequestStatusException(String message) {
        super(message);
    }

    public static IllegalLabRequestStatusException cannotApprove(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot approve request in status: " + currentStatus);
    }

    public static IllegalLabRequestStatusException cannotReject(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot reject request in status: " + currentStatus);
    }

    public static IllegalLabRequestStatusException cannotCollectSample(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot collect sample for request in status: " + currentStatus);
    }

    public static IllegalLabRequestStatusException cannotProcess(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot process request in status: " + currentStatus);
    }

    public static IllegalLabRequestStatusException cannotComplete(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot complete request in status: " + currentStatus);
    }

    public static IllegalLabRequestStatusException cannotCancel(String currentStatus) {
        return new IllegalLabRequestStatusException("Cannot cancel request in status: " + currentStatus);
    }
}
