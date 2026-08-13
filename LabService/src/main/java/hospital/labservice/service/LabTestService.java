package hospital.labservice.service;

import hospital.labservice.dto.labtest.LabTestCreateDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.dto.labtest.LabTestUpdateDto;
import hospital.labservice.model.enums.TestCategory;

import java.util.List;

/**
 * Service interface for LabTest management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each test must have a unique code</li>
 *   <li>Tests can be activated/deactivated without deletion</li>
 *   <li>Inactive tests cannot be ordered but remain visible in history</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabTestService {

    /**
     * Creates a new lab test.
     *
     * @param dto the test creation data
     * @return the created test
     */
    LabTestResponseDto createLabTest(LabTestCreateDto dto);

    /**
     * Gets a lab test by its ID.
     *
     * @param id the test ID
     * @return the test
     */
    LabTestResponseDto getLabTestById(Long id);

    /**
     * Gets a lab test by its unique code.
     *
     * @param code the test code
     * @return the test
     */
    LabTestResponseDto getLabTestByCode(String code);

    /**
     * Gets all lab tests.
     *
     * @return list of all tests
     */
    List<LabTestResponseDto> getAllLabTests();

    /**
     * Gets lab tests by category.
     *
     * @param category the test category
     * @return list of tests in the category
     */
    List<LabTestResponseDto> getLabTestsByCategory(TestCategory category);

    /**
     * Gets active lab tests (available for ordering).
     *
     * @return list of active tests
     */
    List<LabTestResponseDto> getActiveLabTests();

    /**
     * Searches lab tests by name pattern.
     *
     * @param name the search pattern
     * @return list of matching tests
     */
    List<LabTestResponseDto> searchByName(String name);

    /**
     * Updates an existing lab test.
     *
     * @param id  the test ID
     * @param dto the update data
     * @return the updated test
     */
    LabTestResponseDto updateLabTest(Long id, LabTestUpdateDto dto);

    /**
     * Soft-deletes a lab test.
     *
     * @param id the test ID
     */
    void deleteLabTest(Long id);

    /**
     * Checks if a test code already exists.
     *
     * @param code the code to check
     * @return true if exists
     */
    boolean codeExists(String code);
}
