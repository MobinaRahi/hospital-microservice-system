package hospital.labservice.exception.labequipment;

/**
 * Exception thrown when a duplicate equipment serial number is detected.
 *
 * @author MobinaRahi
 */
public class DuplicateLabEquipmentSerialNumberException extends RuntimeException {

    public DuplicateLabEquipmentSerialNumberException(String serialNumber) {
        super("Lab equipment with serial number '" + serialNumber + "' already exists");
    }
}
