package hospital.billingservice.exception.servicecatalog;

/**
 * Exception thrown when a service catalog entry is not found.
 *
 * @author MobinaRahi
 */
public class ServiceCatalogNotFoundException extends RuntimeException {

    public ServiceCatalogNotFoundException(String message) {
        super(message);
    }

    public static ServiceCatalogNotFoundException byId(Long id) {
        return new ServiceCatalogNotFoundException("Service with id " + id + " not found");
    }

    public static ServiceCatalogNotFoundException byCode(String code) {
        return new ServiceCatalogNotFoundException("Service with code '" + code + "' not found");
    }
}
