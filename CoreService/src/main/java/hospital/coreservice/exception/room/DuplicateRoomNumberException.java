package hospital.coreservice.exception.room;

public class DuplicateRoomNumberException extends RuntimeException {
    public DuplicateRoomNumberException(String roomNumber) {
        super("Room number already exists: " + roomNumber);
    }
}