package hospital.adminservice.repository;

import hospital.adminservice.model.Bed;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Bed entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByDepartmentId - Beds in a department</li>
 *   <li>findByStatus - Beds by status</li>
 *   <li>findByType - Beds by type</li>
 *   <li>findByCurrentPatientId - Bed assigned to patient</li>
 *   <li>findAvailableBeds - Available beds count</li>
 *   <li>findOccupiedBeds - Occupied beds count</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface BedRepository extends BaseEntityRepository<Bed, Long> {

    /**
     * Finds beds by department ID.
     *
     * @param departmentId the department ID
     * @return list of beds in the department
     */
    List<Bed> findByDepartmentId(Long departmentId);

    /**
     * Finds beds by status.
     *
     * @param status the bed status
     * @return list of beds with the status
     */
    List<Bed> findByStatus(BedStatus status);

    /**
     * Finds beds by type.
     *
     * @param type the bed type
     * @return list of beds with the type
     */
    List<Bed> findByType(BedType type);

    /**
     * Finds bed currently assigned to a patient.
     *
     * @param patientId the patient ID
     * @return bed if found
     */
    Optional<Bed> findByCurrentPatientId(Long patientId);

    /**
     * Finds available beds in a department.
     *
     * @param departmentId the department ID
     * @return list of available beds
     */
    List<Bed> findByDepartmentIdAndStatus(Long departmentId, BedStatus status);

    /**
     * Counts available beds.
     *
     * @return number of available beds
     */
    @Query("SELECT COUNT(b) FROM Bed b WHERE b.status = 'AVAILABLE' AND b.deleted = false")
    long countAvailableBeds();

    /**
     * Counts occupied beds.
     *
     * @return number of occupied beds
     */
    @Query("SELECT COUNT(b) FROM Bed b WHERE b.status = 'OCCUPIED' AND b.deleted = false")
    long countOccupiedBeds();

    /**
     * Finds beds by bed number pattern.
     *
     * @param bedNumber the bed number pattern
     * @return list of matching beds
     */
    List<Bed> findByBedNumberContainingIgnoreCase(String bedNumber);
}
