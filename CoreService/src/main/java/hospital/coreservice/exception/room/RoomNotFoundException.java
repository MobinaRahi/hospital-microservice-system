package hospital.coreservice.exception.room;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
    public static RoomNotFoundException byId(Long roomId) {
        throw new RoomNotFoundException("Room not found with id: " + roomId);
    }
}
