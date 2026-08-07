package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.exception.supplier.DuplicateSupplierEmailException;
import hospital.inventoryservice.dto.supplier.SupplierCreateDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierUpdateDto;


import hospital.inventoryservice.mapper.SupplierMapper;
import hospital.inventoryservice.exception.supplier.SupplierNotFoundException;
import hospital.inventoryservice.exception.supplier.DuplicateSupplierEmailException;
import hospital.inventoryservice.model.Supplier;
import hospital.inventoryservice.repository.SupplierRepository;
import hospital.inventoryservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link SupplierService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Supplier email must be unique</li>
 *   <li>Suppliers can be deactivated (inactive) instead of deleted</li>
 *   <li>A supplier can be soft-deleted</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public SupplierResponseDto createSupplier(SupplierCreateDto dto) {
        log.info("Creating supplier: {}", dto.getName());

        // Validate email uniqueness
        if (dto.getEmail() != null && emailExists(dto.getEmail())) {
            throw new DuplicateSupplierEmailException(dto.getEmail());
        }

        // Map DTO to entity
        Supplier supplier = supplierMapper.toEntity(dto);
        supplier.setIsActive(true);

        // Save and return
        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier created with id: {}", saved.getId());

        return supplierMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierById(Long id) {
        log.debug("Fetching supplier by id: {}", id);

        Supplier supplier = supplierRepository.findNotDeletedById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id));

        return supplierMapper.toResponseDto(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierByEmail(String email) {
        log.debug("Fetching supplier by email: {}", email);

        Supplier supplier = supplierRepository.findByEmail(email)
                .orElseThrow(() -> SupplierNotFoundException.byEmail(email));

        return supplierMapper.toResponseDto(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDto> getAllActiveSuppliers() {
        log.debug("Fetching all active suppliers");

        List<Supplier> suppliers = supplierRepository.findByIsActiveTrue();
        return supplierMapper.toResponseDtoList(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDto> getAllSuppliers() {
        log.debug("Fetching all suppliers");

        List<Supplier> suppliers = supplierRepository.findAllNotDeleted();
        return supplierMapper.toResponseDtoList(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDto> searchByName(String name) {
        log.debug("Searching suppliers by name: {}", name);

        List<Supplier> suppliers = supplierRepository.findByNameContainingIgnoreCase(name);
        return supplierMapper.toResponseDtoList(suppliers);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public SupplierResponseDto updateSupplier(Long id, SupplierUpdateDto dto) {
        log.info("Updating supplier id: {}", id);

        Supplier supplier = supplierRepository.findNotDeletedById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id));

        // Validate email uniqueness if changed
        if (dto.getEmail() != null && !dto.getEmail().equals(supplier.getEmail())) {
            if (emailExists(dto.getEmail())) {
                throw new DuplicateSupplierEmailException(dto.getEmail());
            }
        }

        // Map update DTO to entity
        supplierMapper.updateEntity(dto, supplier);

        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier updated id: {}", saved.getId());

        return supplierMapper.toResponseDto(saved);
    }

    @Override
    public SupplierResponseDto toggleActive(Long id) {
        log.info("Toggling active status for supplier id: {}", id);

        Supplier supplier = supplierRepository.findNotDeletedById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id));

        supplier.setIsActive(!supplier.getIsActive());
        Supplier saved = supplierRepository.save(supplier);

        return supplierMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteSupplier(Long id) {
        log.info("Soft-deleting supplier id: {}", id);

        Supplier supplier = supplierRepository.findNotDeletedById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id));

        supplier.softDelete(null);
        supplierRepository.save(supplier);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return supplierRepository.existsByEmail(email);
    }
}
