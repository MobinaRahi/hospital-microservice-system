package hospital.inventoryservice.exception.equipmentassignment;

public class EquipmentNotReturnedException extends RuntimeException {

    public EquipmentNotReturnedException(Long assignmentId) {
        super("Equipment assignment with id " + assignmentId + " is still active. Return the equipment first.");
    }
}
