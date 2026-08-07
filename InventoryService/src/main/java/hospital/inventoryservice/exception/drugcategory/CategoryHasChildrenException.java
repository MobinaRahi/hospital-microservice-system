package hospital.inventoryservice.exception.drugcategory;

public class CategoryHasChildrenException extends RuntimeException {

    public CategoryHasChildrenException(Long categoryId) {
        super("Cannot delete category with id " + categoryId + " because it has child categories. Move or delete children first.");
    }
}
