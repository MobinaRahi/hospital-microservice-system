package hospital.inventoryservice.exception.drugcategory;

public class DrugCategoryNotFoundException extends RuntimeException {

    public DrugCategoryNotFoundException(String message) {
        super(message);
    }

    public static DrugCategoryNotFoundException byId(Long id) {
        return new DrugCategoryNotFoundException("Drug category with id " + id + " not found");
    }

    public static DrugCategoryNotFoundException byName(String name) {
        return new DrugCategoryNotFoundException("Drug category with name '" + name + "' not found");
    }
}
