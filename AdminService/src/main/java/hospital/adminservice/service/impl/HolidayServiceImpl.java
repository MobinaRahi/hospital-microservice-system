package hospital.adminservice.service.impl;

import hospital.adminservice.dto.holiday.HolidayCreateDto;
import hospital.adminservice.dto.holiday.HolidayResponseDto;
import hospital.adminservice.dto.holiday.HolidayUpdateDto;
import hospital.adminservice.exception.holiday.HolidayNotFoundException;
import hospital.adminservice.mapper.HolidayMapper;
import hospital.adminservice.model.Holiday;
import hospital.adminservice.repository.HolidayRepository;
import hospital.adminservice.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;

    @Override
    public HolidayResponseDto createHoliday(HolidayCreateDto dto) {
        log.info("Creating holiday: {} on {}", dto.getName(), dto.getDate());

        Holiday holiday = holidayMapper.toEntity(dto);
        Holiday saved = holidayRepository.save(holiday);
        log.info("Holiday created with id: {}", saved.getId());

        return holidayMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayResponseDto getHolidayById(Long id) {
        log.debug("Fetching holiday by id: {}", id);

        Holiday holiday = holidayRepository.findNotDeletedById(id)
                .orElseThrow(() -> HolidayNotFoundException.byId(id));

        return holidayMapper.toResponseDto(holiday);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponseDto> getHolidaysByYear(Integer year) {
        log.debug("Fetching holidays in year: {}", year);

        List<Holiday> holidays = holidayRepository.findByYear(year);
        return holidayMapper.toResponseDtoList(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponseDto> getActiveHolidays() {
        log.debug("Fetching active holidays");

        List<Holiday> holidays = holidayRepository.findByIsActiveTrue();
        return holidayMapper.toResponseDtoList(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponseDto> getRecurringHolidays() {
        log.debug("Fetching recurring holidays");

        List<Holiday> holidays = holidayRepository.findByIsRecurringTrue();
        return holidayMapper.toResponseDtoList(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponseDto> getHolidaysOnDate(LocalDate date) {
        log.debug("Fetching holidays on date: {}", date);

        List<Holiday> holidays = holidayRepository.findHolidaysOnDate(date);
        return holidayMapper.toResponseDtoList(holidays);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.isHolidayOnDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponseDto> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching holidays from {} to {}", startDate, endDate);

        List<Holiday> holidays = holidayRepository.findByDateRange(startDate, endDate);
        return holidayMapper.toResponseDtoList(holidays);
    }

    @Override
    public HolidayResponseDto updateHoliday(Long id, HolidayUpdateDto dto) {
        log.info("Updating holiday id: {}", id);

        Holiday holiday = holidayRepository.findNotDeletedById(id)
                .orElseThrow(() -> HolidayNotFoundException.byId(id));

        holidayMapper.updateEntity(dto, holiday);
        Holiday saved = holidayRepository.save(holiday);
        log.info("Holiday updated id: {}", saved.getId());

        return holidayMapper.toResponseDto(saved);
    }

    @Override
    public void deleteHoliday(Long id) {
        log.info("Soft-deleting holiday id: {}", id);

        Holiday holiday = holidayRepository.findNotDeletedById(id)
                .orElseThrow(() -> HolidayNotFoundException.byId(id));

        holiday.softDelete(null);
        holidayRepository.save(holiday);
    }
}
