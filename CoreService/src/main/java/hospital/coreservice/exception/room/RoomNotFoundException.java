package hospital.coreservice.exception.room;

/**
 * Thrown when a room is not found.
 *
 * @author Mobina
 */
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
    public static RoomNotFoundException byId(Long roomId) {
        throw new RoomNotFoundException("Room not found with id: " + roomId);
    }
    public static RoomNotFoundException byRoomNumber(String roomNumber) {
        throw new RoomNotFoundException("Room not found with room number: " + roomNumber);
    }
}
