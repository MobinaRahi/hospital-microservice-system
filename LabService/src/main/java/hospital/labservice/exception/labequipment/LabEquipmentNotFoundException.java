package hospital.labservice.exception.labequipment;

/**
 * Exception thrown when lab equipment is not found.
 *
 * @author MobinaRahi
 */
public class LabEquipmentNotFoundException extends RuntimeException {

    public LabEquipmentNotFoundException(String message) {
        super(message);
    }

    public static LabEquipmentNotFoundException byId(Long id) {
        return new LabEquipmentNotFoundException("Lab equipment with id " + id + " not found");
    }

    public static LabEquipmentNotFoundException bySerialNumber(String serialNumber) {
        return new LabEquipmentNotFoundException("Lab equipment with serial number '" + serialNumber + "' not found");
    }
}
