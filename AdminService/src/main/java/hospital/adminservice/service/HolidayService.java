package hospital.adminservice.service;

import hospital.adminservice.dto.holiday.HolidayCreateDto;
import hospital.adminservice.dto.holiday.HolidayResponseDto;
import hospital.adminservice.dto.holiday.HolidayUpdateDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Holiday management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Holidays can be one-time (specific date) or recurring (same date every year)</li>
 *   <li>Recurring holidays are checked by month and day only</li>
 *   <li>Active flag allows temporary disabling without deletion</li>
 *   <li>Used for shift scheduling and payroll calculations</li>
 *   <li>Soft delete supported</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface HolidayService {

    /**
     * Creates a new holiday.
     *
     * @param dto the holiday creation data
     * @return the created holiday
     */
    HolidayResponseDto createHoliday(HolidayCreateDto dto);

    /**
     * Gets a holiday by its ID.
     *
     * @param id the holiday ID
     * @return the holiday
     */
    HolidayResponseDto getHolidayById(Long id);

    /**
     * Gets all holidays in a specific year.
     *
     * @param year the year
     * @return list of holidays in the year
     */
    List<HolidayResponseDto> getHolidaysByYear(Integer year);

    /**
     * Gets all active holidays.
     *
     * @return list of active holidays
     */
    List<HolidayResponseDto> getActiveHolidays();

    /**
     * Gets all recurring holidays.
     *
     * @return list of recurring holidays
     */
    List<HolidayResponseDto> getRecurringHolidays();

    /**
     * Gets holidays on a specific date.
     *
     * @param date the date to check
     * @return list of holidays on the date
     */
    List<HolidayResponseDto> getHolidaysOnDate(LocalDate date);

    /**
     * Checks if a specific date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday
     */
    boolean isHoliday(LocalDate date);

    /**
     * Gets holidays in a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of holidays in range
     */
    List<HolidayResponseDto> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Updates an existing holiday.
     *
     * @param id  the holiday ID
     * @param dto the update data
     * @return the updated holiday
     */
    HolidayResponseDto updateHoliday(Long id, HolidayUpdateDto dto);

    /**
     * Soft-deletes a holiday.
     *
     * @param id the holiday ID
     */
    void deleteHoliday(Long id);
}
