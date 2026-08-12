package hospital.billingservice.service.impl;

import hospital.billingservice.dto.servicecatalog.ServiceCatalogCreateDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogResponseDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogUpdateDto;
import hospital.billingservice.exception.servicecatalog.DuplicateServiceCodeException;
import hospital.billingservice.exception.servicecatalog.ServiceCatalogNotFoundException;
import hospital.billingservice.mapper.ServiceCatalogMapper;
import hospital.billingservice.model.ServiceCatalog;
import hospital.billingservice.model.enums.ServiceCategory;
import hospital.billingservice.repository.ServiceCatalogRepository;
import hospital.billingservice.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ServiceCatalogService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceCatalogRepository serviceCatalogRepository;
    private final ServiceCatalogMapper serviceCatalogMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public ServiceCatalogResponseDto createService(ServiceCatalogCreateDto dto) {
        log.info("Creating service: {}", dto.getName());

        // Validate unique code
        if (serviceCatalogRepository.existsByCode(dto.getCode())) {
            throw new DuplicateServiceCodeException(dto.getCode());
        }

        // Map DTO to entity
        ServiceCatalog service = serviceCatalogMapper.toEntity(dto);
        service.setIsActive(true);

        // Save and return
        ServiceCatalog saved = serviceCatalogRepository.save(service);
        log.info("Service created with id: {}", saved.getId());

        return serviceCatalogMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public ServiceCatalogResponseDto getServiceById(Long id) {
        log.debug("Fetching service by id: {}", id);

        ServiceCatalog service = serviceCatalogRepository.findNotDeletedById(id)
                .orElseThrow(() -> ServiceCatalogNotFoundException.byId(id));

        return serviceCatalogMapper.toResponseDto(service);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceCatalogResponseDto getServiceByCode(String code) {
        log.debug("Fetching service by code: {}", code);

        ServiceCatalog service = serviceCatalogRepository.findByCode(code)
                .orElseThrow(() -> ServiceCatalogNotFoundException.byCode(code));

        return serviceCatalogMapper.toResponseDto(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDto> getAllActiveServices() {
        log.debug("Fetching all active services");

        List<ServiceCatalog> services = serviceCatalogRepository.findByIsActiveTrue();
        return serviceCatalogMapper.toResponseDtoList(services);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDto> getAllServices() {
        log.debug("Fetching all services");

        List<ServiceCatalog> services = serviceCatalogRepository.findAllNotDeleted();
        return serviceCatalogMapper.toResponseDtoList(services);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDto> getServicesByCategory(ServiceCategory category) {
        log.debug("Fetching services by category: {}", category);

        List<ServiceCatalog> services = serviceCatalogRepository.findByCategory(category);
        return serviceCatalogMapper.toResponseDtoList(services);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDto> getActiveServicesByCategory(ServiceCategory category) {
        log.debug("Fetching active services by category: {}", category);

        List<ServiceCatalog> services = serviceCatalogRepository.findByCategoryAndIsActiveTrue(category);
        return serviceCatalogMapper.toResponseDtoList(services);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponseDto> searchByName(String name) {
        log.debug("Searching services by name: {}", name);

        List<ServiceCatalog> services = serviceCatalogRepository.findByNameContainingIgnoreCase(name);
        return serviceCatalogMapper.toResponseDtoList(services);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public ServiceCatalogResponseDto updateService(Long id, ServiceCatalogUpdateDto dto) {
        log.info("Updating service id: {}", id);

        ServiceCatalog service = serviceCatalogRepository.findNotDeletedById(id)
                .orElseThrow(() -> ServiceCatalogNotFoundException.byId(id));

        // Map update DTO to entity
        serviceCatalogMapper.updateEntity(dto, service);

        ServiceCatalog saved = serviceCatalogRepository.save(service);
        log.info("Service updated id: {}", saved.getId());

        return serviceCatalogMapper.toResponseDto(saved);
    }

    @Override
    public ServiceCatalogResponseDto toggleActive(Long id) {
        log.info("Toggling active status for service id: {}", id);

        ServiceCatalog service = serviceCatalogRepository.findNotDeletedById(id)
                .orElseThrow(() -> ServiceCatalogNotFoundException.byId(id));

        service.setIsActive(!service.getIsActive());
        ServiceCatalog saved = serviceCatalogRepository.save(service);

        return serviceCatalogMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deleteService(Long id) {
        log.info("Soft-deleting service id: {}", id);

        ServiceCatalog service = serviceCatalogRepository.findNotDeletedById(id)
                .orElseThrow(() -> ServiceCatalogNotFoundException.byId(id));

        service.softDelete(null);
        serviceCatalogRepository.save(service);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return serviceCatalogRepository.existsByCode(code);
    }
}
