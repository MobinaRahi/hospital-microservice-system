package hospital.inventoryservice.exception.equipment;

public class DuplicateEquipmentSerialNumberException extends RuntimeException {

    public DuplicateEquipmentSerialNumberException(String serialNumber) {
        super("Equipment with serial number '" + serialNumber + "' already exists");
    }
}
