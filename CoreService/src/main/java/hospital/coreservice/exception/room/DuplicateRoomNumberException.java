package hospital.coreservice.exception.room;

/**
 * Thrown when attempting to create a duplicate room.
 *
 * @author Mobina
 */
public class DuplicateRoomNumberException extends RuntimeException {
    public DuplicateRoomNumberException(String roomNumber) {
        super("Room number already exists: " + roomNumber);
    }
}