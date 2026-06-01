package hospital.coreservice.exception.room;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(Long roomId) {
        super("Room " + roomId + "NotAvailable ");
    }
}
