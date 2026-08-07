package hospital.inventoryservice.exception.equipment;

public class EquipmentNotFoundException extends RuntimeException {

    public EquipmentNotFoundException(String message) {
        super(message);
    }

    public static EquipmentNotFoundException byId(Long id) {
        return new EquipmentNotFoundException("Equipment with id " + id + " not found");
    }

    public static EquipmentNotFoundException bySerialNumber(String serialNumber) {
        return new EquipmentNotFoundException("Equipment with serial number '" + serialNumber + "' not found");
    }
}
