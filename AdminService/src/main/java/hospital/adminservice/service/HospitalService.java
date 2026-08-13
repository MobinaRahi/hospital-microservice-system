package hospital.adminservice.service;

import hospital.adminservice.dto.hospital.HospitalCreateDto;
import hospital.adminservice.dto.hospital.HospitalResponseDto;
import hospital.adminservice.dto.hospital.HospitalUpdateDto;

import java.util.List;

/**
 * Service interface for Hospital management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each hospital must have a unique code</li>
 *   <li>Hospitals can be soft-deleted</li>
 *   <li>Multi-tenant: each hospital belongs to a tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface HospitalService {

    /**
     * Creates a new hospital.
     *
     * @param dto the hospital creation data
     * @return the created hospital
     */
    HospitalResponseDto createHospital(HospitalCreateDto dto);

    /**
     * Gets a hospital by its ID.
     *
     * @param id the hospital ID
     * @return the hospital
     */
    HospitalResponseDto getHospitalById(Long id);

    /**
     * Gets a hospital by its unique code.
     *
     * @param code the hospital code
     * @return the hospital
     */
    HospitalResponseDto getHospitalByCode(String code);

    /**
     * Gets all hospitals.
     *
     * @return list of all hospitals
     */
    List<HospitalResponseDto> getAllHospitals();

    /**
     * Updates an existing hospital.
     *
     * @param id  the hospital ID
     * @param dto the update data
     * @return the updated hospital
     */
    HospitalResponseDto updateHospital(Long id, HospitalUpdateDto dto);

    /**
     * Soft-deletes a hospital.
     *
     * @param id the hospital ID
     */
    void deleteHospital(Long id);

    /**
     * Checks if a hospital code is already in use.
     *
     * @param code the code to check
     * @return true if the code exists
     */
    boolean codeExists(String code);
}
