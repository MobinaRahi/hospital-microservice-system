package hospital.coreservice.repository;

import hospital.coreservice.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for shift entity.
 *
 * @author Mobina
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // ========== Update Operations (Status Management) ==========

    /**
     * Deactivates a shift (soft delete).
     */
    @Modifying
    @Query("UPDATE shiftEntity s SET s.isActive = false WHERE s.id = :shiftId")
    void deactivate(@Param("shiftId") Long shiftId);

    /**
     * Activates a shift.
     */
    @Modifying
    @Query("UPDATE shiftEntity s SET s.isActive = true WHERE s.id = :shiftId")
    void activate(@Param("shiftId") Long shiftId);

    // ========== Find by Name ==========

    /**
     * Finds a shift by its exact name (unique).
     */
    Optional<Shift> findByName(String name);

    // ========== Find by Shift Properties ==========

    /**
     * Returns all night shifts.
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = true")
    List<Shift> findAllNightShifts();

    /**
     * Returns all non-night (day) shifts.
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = false")
    List<Shift> findAllDayShifts();

    /**
     * Returns all shifts that have extra pay.
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.hasExtraPay = true")
    List<Shift> findAllShiftsWithExtraPay();

    // ========== Find by Status ==========

    /**
     * Returns all active shifts.
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.isActive = true")
    List<Shift> findActiveShifts();

    /**
     * Returns all inactive shifts.
     */
    @Query("SELECT s FROM shiftEntity s WHERE s.isActive = false")
    List<Shift> findInactiveShifts();

    // ========== Count Operations ==========

    /**
     * Counts all active shifts.
     */
    @Query("SELECT COUNT(s) FROM shiftEntity s WHERE s.isActive = true")
    Long countActiveShifts();

    /**
     * Counts all inactive shifts.
     */
    @Query("SELECT COUNT(s) FROM shiftEntity s WHERE s.isActive = false")
    Long countInactiveShifts();

    // ========== Existence Check ==========

    /**
     * Checks whether a shift with the given name exists.
     */
    boolean existsByName(String name);
}