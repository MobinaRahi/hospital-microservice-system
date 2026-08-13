package hospital.adminservice.repository;

import hospital.adminservice.model.Holiday;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Holiday entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByYear - Holidays in a year</li>
 *   <li>findByIsActiveTrue - Active holidays</li>
 *   <li>findRecurringHolidays - Recurring holidays</li>
 *   <li>isHolidayOnDate - Check if date is holiday</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface HolidayRepository extends BaseEntityRepository<Holiday, Long> {

    /**
     * Finds holidays in a specific year.
     *
     * @param year the year
     * @return list of holidays in the year
     */
    List<Holiday> findByYear(Integer year);

    /**
     * Finds all active holidays.
     *
     * @return list of active holidays
     */
    List<Holiday> findByIsActiveTrue();

    /**
     * Finds all recurring holidays.
     *
     * @return list of recurring holidays
     */
    List<Holiday> findByIsRecurringTrue();

    /**
     * Finds holidays on a specific date.
     *
     * @param date the date to check
     * @return list of holidays on the date
     */
    @Query("SELECT h FROM Holiday h WHERE h.date = :date AND h.deleted = false")
    List<Holiday> findHolidaysOnDate(@Param("date") LocalDate date);

    /**
     * Checks if a specific date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday
     */
    @Query("SELECT COUNT(h) > 0 FROM Holiday h WHERE h.date = :date AND h.isActive = true AND h.deleted = false")
    boolean isHolidayOnDate(@Param("date") LocalDate date);

    /**
     * Finds holidays in a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of holidays in range
     */
    @Query("SELECT h FROM Holiday h WHERE h.date BETWEEN :startDate AND :endDate AND h.deleted = false")
    List<Holiday> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
