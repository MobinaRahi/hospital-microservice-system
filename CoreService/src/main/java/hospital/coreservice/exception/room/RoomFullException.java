package hospital.coreservice.exception.room;

/**
 * Exception for room operations.
 *
 * @author Mobina
 */
public class RoomFullException extends RuntimeException {
    public RoomFullException(Long roomId, int capacity) {
        super("Room " + roomId + " is full (capacity: " + capacity + ")");
    }
}
