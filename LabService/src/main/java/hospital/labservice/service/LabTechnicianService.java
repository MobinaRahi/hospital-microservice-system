package hospital.labservice.service;

import hospital.labservice.dto.labtechnician.LabTechnicianCreateDto;
import hospital.labservice.dto.labtechnician.LabTechnicianResponseDto;
import hospital.labservice.dto.labtechnician.LabTechnicianUpdateDto;
import hospital.labservice.model.enums.LabShift;

import java.util.List;

/**
 * Service interface for LabTechnician management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each technician has a unique userId linked to AuthService</li>
 *   <li>Each technician has a unique employeeCode</li>
 *   <li>Technicians can be activated/deactivated</li>
 *   <li>Shift assignment determines work schedule</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabTechnicianService {

    /**
     * Creates a new lab technician.
     *
     * @param dto the technician creation data
     * @return the created technician
     */
    LabTechnicianResponseDto createTechnician(LabTechnicianCreateDto dto);

    /**
     * Gets a lab technician by their ID.
     *
     * @param id the technician ID
     * @return the technician
     */
    LabTechnicianResponseDto getTechnicianById(Long id);

    /**
     * Gets a lab technician by their AuthService user ID.
     *
     * @param userId the user ID from AuthService
     * @return the technician
     */
    LabTechnicianResponseDto getTechnicianByUserId(Long userId);

    /**
     * Gets all lab technicians.
     *
     * @return list of all technicians
     */
    List<LabTechnicianResponseDto> getAllTechnicians();

    /**
     * Gets technicians by shift.
     *
     * @param shift the work shift
     * @return list of technicians on the shift
     */
    List<LabTechnicianResponseDto> getTechniciansByShift(LabShift shift);

    /**
     * Gets active technicians on a specific shift.
     *
     * @param shift the work shift
     * @return list of active technicians on the shift
     */
    List<LabTechnicianResponseDto> getActiveTechniciansByShift(LabShift shift);

    /**
     * Gets active technicians.
     *
     * @return list of active technicians
     */
    List<LabTechnicianResponseDto> getActiveTechnicians();

    /**
     * Gets technicians by specialization.
     *
     * @param specialization the area of specialization
     * @return list of technicians with the specialization
     */
    List<LabTechnicianResponseDto> getTechniciansBySpecialization(String specialization);

    /**
     * Activates a technician.
     *
     * @param id the technician ID
     * @return the activated technician
     */
    LabTechnicianResponseDto activateTechnician(Long id);

    /**
     * Deactivates a technician.
     *
     * @param id the technician ID
     * @return the deactivated technician
     */
    LabTechnicianResponseDto deactivateTechnician(Long id);

    /**
     * Changes a technician's shift assignment.
     *
     * @param id      the technician ID
     * @param newShift the new shift
     * @return the updated technician
     */
    LabTechnicianResponseDto changeShift(Long id, LabShift newShift);

    /**
     * Updates an existing technician.
     *
     * @param id  the technician ID
     * @param dto the update data
     * @return the updated technician
     */
    LabTechnicianResponseDto updateTechnician(Long id, LabTechnicianUpdateDto dto);

    /**
     * Soft-deletes a lab technician.
     *
     * @param id the technician ID
     */
    void deleteTechnician(Long id);

    /**
     * Checks if a user ID is already linked to a technician.
     *
     * @param userId the user ID to check
     * @return true if exists
     */
    boolean userIdExists(Long userId);

    /**
     * Checks if an employee code already exists.
     *
     * @param employeeCode the employee code to check
     * @return true if exists
     */
    boolean employeeCodeExists(String employeeCode);
}
