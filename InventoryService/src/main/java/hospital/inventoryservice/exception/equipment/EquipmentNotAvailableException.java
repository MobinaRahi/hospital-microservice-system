package hospital.inventoryservice.exception.equipment;

public class EquipmentNotAvailableException extends RuntimeException {

    public EquipmentNotAvailableException(Long equipmentId) {
        super("Equipment with id " + equipmentId + " is not AVAILABLE and cannot be assigned");
    }
}
