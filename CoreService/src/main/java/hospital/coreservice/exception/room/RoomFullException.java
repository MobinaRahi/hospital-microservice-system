package hospital.coreservice.exception.room;

public class RoomFullException extends RuntimeException {
    public RoomFullException(Long roomId, int capacity) {
        super("Room " + roomId + " is full (capacity: " + capacity + ")");
    }
}
