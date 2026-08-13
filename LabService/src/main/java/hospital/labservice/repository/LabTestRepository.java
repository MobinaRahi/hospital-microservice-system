package hospital.labservice.repository;

import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.TestCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for LabTest entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByCode - Find test by unique code (e.g., "CBC", "GLU")</li>
 *   <li>findByNameContaining - Search tests by name pattern</li>
 *   <li>findByCategory - Tests in a specific category</li>
 *   <li>findByCategoryAndIsActive - Active tests in a category</li>
 *   <li>findByIsActive - All active/inactive tests</li>
 *   <li>existsByCode - Check code uniqueness</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabTestRepository extends BaseEntityRepository<LabTest, Long> {

    /**
     * Finds a lab test by its unique code.
     *
     * @param code the test code (e.g., "CBC", "GLU")
     * @return lab test if found
     */
    Optional<LabTest> findByCode(String code);

    /**
     * Finds lab tests whose name contains the given string (case-insensitive).
     * Used for search functionality.
     *
     * @param name the search pattern
     * @return list of matching lab tests
     */
    List<LabTest> findByNameContainingIgnoreCase(String name);

    /**
     * Finds lab tests by category.
     *
     * @param category the test category (HEMATOLOGY, BIOCHEMISTRY, etc.)
     * @return list of tests in the category
     */
    List<LabTest> findByCategory(TestCategory category);

    /**
     * Finds active tests in a specific category.
     * Used for test ordering forms.
     *
     * @param category the test category
     * @param isActive whether the test is active
     * @return list of matching tests
     */
    List<LabTest> findByCategoryAndIsActive(TestCategory category, Boolean isActive);

    /**
     * Finds all active lab tests.
     * Used for test selection dropdowns and ordering.
     *
     * @param isActive whether the test is active
     * @return list of active/inactive tests
     */
    List<LabTest> findByIsActive(Boolean isActive);

    /**
     * Checks if a test with the given code already exists.
     * Used for uniqueness validation before creating a new test.
     *
     * @param code the test code to check
     * @return true if a test with this code exists
     */
    boolean existsByCode(String code);

    /**
     * Finds tests by code pattern (case-insensitive partial match).
     *
     * @param code the code pattern
     * @return list of matching tests
     */
    List<LabTest> findByCodeContainingIgnoreCase(String code);

    /**
     * Counts active tests.
     *
     * @param isActive whether the test is active
     * @return number of active/inactive tests
     */
    long countByIsActive(Boolean isActive);
}
