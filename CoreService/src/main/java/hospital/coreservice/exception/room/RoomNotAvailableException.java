package hospital.coreservice.exception.room;

/**
 * Exception for room operations.
 *
 * @author Mobina
 */
public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(Long roomId) {
        super("Room " + roomId + "NotAvailable ");
    }
}
