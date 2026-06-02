package hospital.coreservice.service.imp;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.exception.doctor_schedule.DoctorScheduleNotFoundException;
import hospital.coreservice.exception.doctor_schedule.DuplicateDoctorScheduleException;
import hospital.coreservice.mapper.DoctorScheduleMapper;
import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.repository.DoctorScheduleRepository;
import hospital.coreservice.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorScheduleService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public DoctorScheduleResponseDto createDoctorSchedule(DoctorScheduleCreateDto createDto) {
        log.info("Creating new doctor schedule for doctor id: {} on day: {}", createDto.getDoctorId(), createDto.getDayOfWeek());
        DoctorSchedule schedule = doctorScheduleMapper.toEntity(createDto);
        DoctorSchedule saved = doctorScheduleRepository.save(schedule);
        log.info("Doctor schedule created with id: {}", saved.getId());
        return doctorScheduleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public DoctorScheduleResponseDto updateDoctorSchedule(Long scheduleId, DoctorScheduleUpdateDto updateDto) {
        log.info("Updating doctor schedule with id: {}", scheduleId);
        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byId(scheduleId));
        doctorScheduleMapper.updateEntity(schedule, updateDto);
        DoctorSchedule updated = doctorScheduleRepository.save(schedule);
        log.info("Doctor schedule updated with id: {}", updated.getId());
        return doctorScheduleMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void bulkCreateDoctorSchedules(List<DoctorScheduleCreateDto> createDtoList) {
        log.info("Bulk creating {} doctor schedules", createDtoList.size());

        // Check for duplicate schedules
        DoctorScheduleCreateDto duplicate = createDtoList.stream()
                .filter(dto -> doctorScheduleRepository.existsByDoctorIdAndDayOfWeek(dto.getDoctorId(), dto.getDayOfWeek()))
                .findFirst()
                .orElse(null);

        if (duplicate != null) {
            throw new DuplicateDoctorScheduleException(duplicate.getDoctorId(), duplicate.getDayOfWeek());
        }

        List<DoctorSchedule> schedules = createDtoList.stream()
                .map(doctorScheduleMapper::toEntity)
                .collect(Collectors.toList());

        doctorScheduleRepository.saveAll(schedules);
        log.info("Successfully bulk created {} doctor schedules", schedules.size());
    }

    // ========== Basic Retrieval ==========

    @Override
    public DoctorScheduleResponseDto getDoctorScheduleById(Long scheduleId) {
        log.debug("Fetching doctor schedule by id: {}", scheduleId);
        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byId(scheduleId));
        return doctorScheduleMapper.toResponseDto(schedule);
    }

    @Override
    public List<DoctorScheduleResponseDto> getDoctorSchedulesByDoctorId(Long doctorId) {
        log.debug("Fetching all schedules for doctor id: {}", doctorId);
        return doctorScheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getAllDoctorSchedules() {
        log.debug("Fetching all doctor schedules");
        return doctorScheduleRepository.findAll()
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Single Day Retrieval ==========

    @Override
    public DoctorScheduleResponseDto getDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek) {
        log.debug("Fetching schedule for doctor: {} on day: {}", doctorId, dayOfWeek);
        DoctorSchedule schedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byDoctorIdAndDayOfWeek(doctorId, dayOfWeek));
        return doctorScheduleMapper.toResponseDto(schedule);
    }

    @Override
    public DoctorScheduleResponseDto getActiveDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek) {
        log.debug("Fetching active schedule for doctor: {} on day: {}", doctorId, dayOfWeek);
        DoctorSchedule schedule = doctorScheduleRepository.findActiveByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                .orElseThrow(() -> DoctorScheduleNotFoundException.activeScheduleNotFoundForDoctorAndDay(doctorId, dayOfWeek));
        return doctorScheduleMapper.toResponseDto(schedule);
    }

    // ========== Status Management ==========

    @Override
    @Transactional
    public void activateDoctorSchedule(Long scheduleId) {
        log.info("Activating doctor schedule with id: {}", scheduleId);
        doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byId(scheduleId));
        doctorScheduleRepository.activate(scheduleId);
    }

    @Override
    @Transactional
    public void deactivateDoctorSchedule(Long scheduleId) {
        log.warn("Deactivating doctor schedule with id: {}", scheduleId);
        doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> DoctorScheduleNotFoundException.byId(scheduleId));
        doctorScheduleRepository.deactivate(scheduleId);
    }

    @Override
    public List<DoctorScheduleResponseDto> getActiveDoctorSchedules() {
        log.debug("Fetching all active doctor schedules");
        return doctorScheduleRepository.findAllActive()
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByDoctorId(Long doctorId) {
        log.debug("Fetching active schedules for doctor id: {}", doctorId);
        return doctorScheduleRepository.findActiveByDoctorId(doctorId)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getInactiveDoctorSchedules() {
        log.debug("Fetching all inactive doctor schedules");
        return doctorScheduleRepository.findAllInactive()
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getInactiveDoctorSchedulesByDoctorId(Long doctorId) {
        log.debug("Fetching inactive schedules for doctor id: {}", doctorId);
        return doctorScheduleRepository.findInactiveByDoctorId(doctorId)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Time Based Queries ==========

    @Override
    public List<DoctorScheduleResponseDto> getDoctorSchedulesByStartTimeAfter(LocalTime time) {
        log.debug("Fetching schedules with start time after: {}", time);
        return doctorScheduleRepository.findByStartTimeAfter(time)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByStartTimeAfter(LocalTime time) {
        log.debug("Fetching activeSchedules with start time after: {}", time);
        return doctorScheduleRepository.findActiveByStartTimeAfter(time)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getDoctorSchedulesByEndTimeBefore(LocalTime time) {
        log.debug("Fetching schedules with end time before: {}", time);
        return doctorScheduleRepository.findByEndTimeBefore(time)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByEndTimeBefore(LocalTime time) {
        log.debug("Fetching activeSchedules with end time before: {}", time);
        return doctorScheduleRepository.findActiveByEndTimeBefore(time)
                .stream()
                .map(doctorScheduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Validation ==========

    @Override
    public boolean existsByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek) {
        log.debug("Checking existence for doctor: {} on day: {}", doctorId, dayOfWeek);
        return doctorScheduleRepository.existsByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
    }
}