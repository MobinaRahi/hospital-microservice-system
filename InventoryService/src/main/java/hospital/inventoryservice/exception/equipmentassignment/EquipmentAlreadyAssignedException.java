package hospital.inventoryservice.exception.equipmentassignment;

public class EquipmentAlreadyAssignedException extends RuntimeException {

    public EquipmentAlreadyAssignedException(Long equipmentId) {
        super("Equipment with id " + equipmentId + " is already assigned and cannot be deleted");
    }
}
