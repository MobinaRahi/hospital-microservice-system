package hospital.coreservice.repository;

import hospital.coreservice.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Shift entity.
 * Provides database operations for shift management.
 * <p>
 * Shifts represent work periods (Morning, Evening, Night, On-Call)
 * that can be assigned to nurses and other hospital staff.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // ========== Update Operations (Status Management) ==========

    /**
     * Deactivates a shift (soft delete).
     * Sets isActive = false for the specified shift.
     * Inactive shifts are not available for new assignments.
     *
     * @param shiftId the ID of the shift to deactivate
     */
    @Modifying
    @Query("UPDATE shiftEntity s SET s.isActive = false WHERE s.id = :shiftId")
    void deactivate(@Param("shiftId") Long shiftId);

    /**
     * Activates a shift.
     * Sets isActive = true for the specified shift.
     * Active shifts are available for nurse assignments.
     *
     * @param shiftId the ID of the shift to activate
     */
    @Modifying
    @Query("UPDATE shiftEntity s SET s.isActive = true WHERE s.id = :shiftId")
    void activate(@Param("shiftId") Long shiftId);

    // ========== Find by Name ==========

    /**
     * Finds shifts by exact name.
     * <p>
     * Common shift names: "Morning", "Evening", "Night", "On-Call"
     * </p>
     *
     * @param name the exact shift name to search for
     * @return list of shifts with the given name
     */
    List<Shift> findByName(@Param("name") String name);

    // ========== Find by Properties ==========

    /**
     * Finds all night shifts.
     * Night shifts typically have extra pay and different working hours.
     *
     * @return list of all night shifts
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = true")
    List<Shift> findAllNightShifts();

    /**
     * Finds all shifts that include extra pay/overtime compensation.
     * These are typically night shifts, holiday shifts, or on-call shifts.
     *
     * @return list of shifts with extra pay
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.hasExtraPay = true")
    List<Shift> findAllShiftsWithExtraPay();

    // ========== Find by Status ==========

    /**
     * Finds all active shifts.
     * Active shifts are currently available for nurse assignments.
     *
     * @return list of all active shifts
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.isActive = true")
    List<Shift> findActiveShifts();

    /**
     * Finds all inactive shifts.
     * Inactive shifts are deprecated and not used in the system.
     *
     * @return list of all inactive shifts
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.isActive = false")
    List<Shift> findInactiveShifts();
}
