package hospital.inventoryservice.exception.equipmentassignment;

public class EquipmentAssignmentNotFoundException extends RuntimeException {

    public EquipmentAssignmentNotFoundException(String message) {
        super(message);
    }

    public static EquipmentAssignmentNotFoundException byId(Long id) {
        return new EquipmentAssignmentNotFoundException("Equipment assignment with id " + id + " not found");
    }
}
