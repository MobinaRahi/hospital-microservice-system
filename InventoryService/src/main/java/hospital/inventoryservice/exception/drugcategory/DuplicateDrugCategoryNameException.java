package hospital.inventoryservice.exception.drugcategory;

public class DuplicateDrugCategoryNameException extends RuntimeException {

    public DuplicateDrugCategoryNameException(String name) {
        super("Drug category with name '" + name + "' already exists at this level");
    }
}
