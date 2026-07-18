package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemCreateDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemResponseDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemUpdateDto;

import java.util.List;

/**
 * Service interface for PrescriptionItem management.
 * Handles CRUD operations for drug items within prescriptions.
 *
 * @author Mobina
 */
public interface PrescriptionItemService {

    PrescriptionItemResponseDto createPrescriptionItem(PrescriptionItemCreateDto createDto);

    PrescriptionItemResponseDto updatePrescriptionItem(Long id, PrescriptionItemUpdateDto updateDto);

    void deletePrescriptionItem(Long id);

    PrescriptionItemResponseDto getPrescriptionItemById(Long id);

    List<PrescriptionItemResponseDto> getPrescriptionItemsByPrescriptionId(Long prescriptionId);

    List<PrescriptionItemResponseDto> getPrescriptionItemsByDrugId(Long drugId);

    List<PrescriptionItemResponseDto> searchPrescriptionItemsByDrugName(String drugName);

    List<PrescriptionItemResponseDto> getPrescriptionItemsByPatientId(Long patientId);

    Long countPrescriptionItemsByPrescriptionId(Long prescriptionId);
}
