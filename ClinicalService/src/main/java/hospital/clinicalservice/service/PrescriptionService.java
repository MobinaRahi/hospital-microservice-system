package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.prescription.PrescriptionCreateDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionUpdateDto;
import hospital.clinicalservice.model.enums.PrescriptionStatus;

import java.util.List;

/**
 * Service interface for Prescription management.
 * Handles CRUD and status workflow for prescriptions.
 *
 * @author Mobina
 */
public interface PrescriptionService {

    PrescriptionResponseDto createPrescription(PrescriptionCreateDto createDto);

    PrescriptionResponseDto updatePrescription(Long id, PrescriptionUpdateDto updateDto);

    void cancelPrescription(Long id);

    void completePrescription(Long id);

    PrescriptionResponseDto getPrescriptionById(Long id);

    List<PrescriptionResponseDto> getPrescriptionsByEncounterId(Long encounterId);

    List<PrescriptionResponseDto> getPrescriptionsByPatientId(Long patientId);

    List<PrescriptionResponseDto> getActivePrescriptionsByPatientId(Long patientId);

    List<PrescriptionResponseDto> getPrescriptionsByDoctorId(Long doctorId);

    List<PrescriptionResponseDto> getPrescriptionsByStatus(PrescriptionStatus status);

    List<PrescriptionResponseDto> getExpiredPrescriptions();

    Long countPrescriptionsByPatientId(Long patientId);

    Long countPrescriptionsByDoctorId(Long doctorId);

    Long countPrescriptionsByStatus(PrescriptionStatus status);
}
