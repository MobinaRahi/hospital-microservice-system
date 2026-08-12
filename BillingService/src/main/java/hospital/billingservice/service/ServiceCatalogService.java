package hospital.billingservice.service;

import hospital.billingservice.dto.servicecatalog.ServiceCatalogCreateDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogResponseDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogUpdateDto;
import hospital.billingservice.model.enums.ServiceCategory;

import java.util.List;

/**
 * Service interface for ServiceCatalog.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Service code must be unique</li>
 *   <li>Price must be positive</li>
 *   <li>Inactive services cannot be added to new invoices</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface ServiceCatalogService {

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new service catalog entry.
     *
     * @param dto the service creation data
     * @return the created service
     */
    ServiceCatalogResponseDto createService(ServiceCatalogCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Gets a service by its ID.
     *
     * @param id the service ID
     * @return the service
     */
    ServiceCatalogResponseDto getServiceById(Long id);

    /**
     * Gets a service by its unique code.
     *
     * @param code the service code
     * @return the service
     */
    ServiceCatalogResponseDto getServiceByCode(String code);

    /**
     * Gets all active services.
     *
     * @return list of active services
     */
    List<ServiceCatalogResponseDto> getAllActiveServices();

    /**
     * Gets all services (including inactive).
     *
     * @return list of all services
     */
    List<ServiceCatalogResponseDto> getAllServices();

    /**
     * Gets services by category.
     *
     * @param category the service category
     * @return list of services in the category
     */
    List<ServiceCatalogResponseDto> getServicesByCategory(ServiceCategory category);

    /**
     * Gets active services by category.
     *
     * @param category the service category
     * @return list of active services in the category
     */
    List<ServiceCatalogResponseDto> getActiveServicesByCategory(ServiceCategory category);

    /**
     * Searches services by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching services
     */
    List<ServiceCatalogResponseDto> searchByName(String name);

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing service.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the service ID
     * @param dto the update data
     * @return the updated service
     */
    ServiceCatalogResponseDto updateService(Long id, ServiceCatalogUpdateDto dto);

    /**
     * Toggles the active status of a service.
     *
     * @param id the service ID
     * @return the updated service
     */
    ServiceCatalogResponseDto toggleActive(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a service.
     *
     * @param id the service ID
     */
    void deleteService(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if a service code is already in use.
     *
     * @param code the code to check
     * @return true if the code exists
     */
    boolean codeExists(String code);
}
