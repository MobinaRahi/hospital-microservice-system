package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.dto.drug.DrugCreateDto;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import hospital.inventoryservice.dto.drug.DrugUpdateDto;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.exception.drug.DuplicateDrugBarcodeException;
import hospital.inventoryservice.exception.drugcategory.DrugCategoryNotFoundException;
import hospital.inventoryservice.mapper.DrugMapper;
import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.DrugCategory;
import hospital.inventoryservice.model.enums.DrugForm;
import hospital.inventoryservice.repository.DrugCategoryRepository;
import hospital.inventoryservice.repository.DrugRepository;
import hospital.inventoryservice.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link DrugService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Generic name is immutable after creation</li>
 *   <li>Barcode must be unique across all drugs</li>
 *   <li>Category must exist before creating a drug</li>
 *   <li>Drugs can be soft-deleted</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugServiceImpl implements DrugService {

    private final DrugRepository drugRepository;
    private final DrugCategoryRepository categoryRepository;
    private final DrugMapper drugMapper;

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public DrugResponseDto createDrug(DrugCreateDto dto) {
        log.info("Creating drug: {}", dto.getGenericName());

        // Validate barcode uniqueness
        if (dto.getBarcode() != null && barcodeExists(dto.getBarcode())) {
            throw new DuplicateDrugBarcodeException(dto.getBarcode());
        }

        // Validate category exists
        DrugCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> DrugCategoryNotFoundException.byId(dto.getCategoryId()));

        // Map DTO to entity
        Drug drug = drugMapper.toEntity(dto);
        drug.setCategory(category);

        // Save and return
        Drug saved = drugRepository.save(drug);
        log.info("Drug created with id: {}", saved.getId());

        return drugMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public DrugResponseDto getDrugById(Long id) {
        log.debug("Fetching drug by id: {}", id);

        Drug drug = drugRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugNotFoundException.byId(id));

        return drugMapper.toResponseDto(drug);
    }

    @Override
    @Transactional(readOnly = true)
    public DrugResponseDto getDrugByBarcode(String barcode) {
        log.debug("Fetching drug by barcode: {}", barcode);

        Drug drug = drugRepository.findByBarcode(barcode)
                .orElseThrow(() -> DrugNotFoundException.byBarcode(barcode));

        return drugMapper.toResponseDto(drug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getAllDrugs() {
        log.debug("Fetching all drugs");

        List<Drug> drugs = drugRepository.findAllNotDeleted();
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> searchByGenericName(String name) {
        log.debug("Searching drugs by generic name: {}", name);

        List<Drug> drugs = drugRepository.findByGenericNameContainingIgnoreCase(name);
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getDrugsByCategory(Long categoryId) {
        log.debug("Fetching drugs by category: {}", categoryId);

        List<Drug> drugs = drugRepository.findByCategoryId(categoryId);
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getDrugsByForm(DrugForm form) {
        log.debug("Fetching drugs by form: {}", form);

        List<Drug> drugs = drugRepository.findByForm(form);
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getPrescriptionDrugs() {
        log.debug("Fetching prescription drugs");

        List<Drug> drugs = drugRepository.findByRequiresPrescriptionTrue();
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getDrugsWithLowStock() {
        log.debug("Fetching drugs with low stock");

        List<Drug> drugs = drugRepository.findDrugsWithLowStock();
        return drugMapper.toResponseDtoList(drugs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugResponseDto> getDrugsWithExpiringStock(int daysThreshold) {
        log.debug("Fetching drugs with expiring stock within {} days", daysThreshold);

        LocalDate expiryDate = LocalDate.now().plusDays(daysThreshold);
        List<Drug> drugs = drugRepository.findDrugsWithExpiringStock(expiryDate);
        return drugMapper.toResponseDtoList(drugs);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public DrugResponseDto updateDrug(Long id, DrugUpdateDto dto) {
        log.info("Updating drug id: {}", id);

        Drug drug = drugRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugNotFoundException.byId(id));

        // Validate barcode uniqueness if changed
        if (dto.getBarcode() != null && !dto.getBarcode().equals(drug.getBarcode())) {
            if (barcodeExists(dto.getBarcode())) {
                throw new DuplicateDrugBarcodeException(dto.getBarcode());
            }
        }

        // Map update DTO to entity
        drugMapper.updateEntity(dto, drug);

        Drug saved = drugRepository.save(drug);
        log.info("Drug updated id: {}", saved.getId());

        return drugMapper.toResponseDto(saved);
    }

    @Override
    public DrugResponseDto toggleActive(Long id) {
        log.info("Toggling active status for drug id: {}", id);

        Drug drug = drugRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugNotFoundException.byId(id));

        drug.setIsActive(!drug.getIsActive());
        Drug saved = drugRepository.save(drug);

        return drugMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteDrug(Long id) {
        log.info("Soft-deleting drug id: {}", id);

        Drug drug = drugRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugNotFoundException.byId(id));

        drug.softDelete(null);
        drugRepository.save(drug);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean barcodeExists(String barcode) {
        return drugRepository.existsByBarcode(barcode);
    }
}
